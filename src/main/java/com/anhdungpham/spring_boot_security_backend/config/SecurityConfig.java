package com.anhdungpham.spring_boot_security_backend.config;

import com.anhdungpham.spring_boot_security_backend.auth.CustomUserDetailsService;
import com.anhdungpham.spring_boot_security_backend.jwt.JwtOncePerRequestFilter;
import com.anhdungpham.spring_boot_security_backend.jwt.JwtSigning;
import com.anhdungpham.spring_boot_security_backend.jwt.JwtUsernamePasswordAuthenticationFilter;
import com.anhdungpham.spring_boot_security_backend.utils.JwtAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtSigning jwtSigning;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(customUserDetailsService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter =
                new JwtUsernamePasswordAuthenticationFilter(
                        authenticationManagerBean(), jwtSigning
                );
        jwtUsernamePasswordAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http.cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        List<String> domain = List.of("http://localhost:3000", "https://localhost:3000",
                                "http://phamanhdung1813.github.io/ui_spring_reactjs", "https://phamanhdung1813.github.io/ui_spring_reactjs",
                                "http://ui-spring-reactjs.herokuapp.com", "https://ui-spring-reactjs.herokuapp.com");
                        config.setAllowedOrigins(domain);
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                })
                .and().rememberMe().tokenValiditySeconds(3600).key(jwtSigning.getSecretKey())

                // JWT dont need SESSION ID and SERVER MEMORY, only PAYLOAD AND SIGNATURE
                .and().csrf().disable()

//        ignoringAntMatchers("/api/auth/login/**").csrfTokenRepository(
//                        CookieCsrfTokenRepository.withHttpOnlyFalse())

                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)

                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().authorizeRequests().antMatchers("/api/auth/login/**",
                        "/api/auth/refresh_token/**").permitAll()

                .and().addFilter(jwtUsernamePasswordAuthenticationFilter)
                .addFilterAfter(new JwtOncePerRequestFilter(jwtSigning), JwtUsernamePasswordAuthenticationFilter.class)

                .authorizeRequests().anyRequest().authenticated()
                .and().httpBasic();
    }
}

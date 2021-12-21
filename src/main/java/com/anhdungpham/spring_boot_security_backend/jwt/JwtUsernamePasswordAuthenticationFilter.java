package com.anhdungpham.spring_boot_security_backend.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtSigning jwtSigning;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        JwtRequestUsernamePasswordDto jwtRequestUsernamePasswordDto = new ObjectMapper().readValue(
                request.getInputStream(), JwtRequestUsernamePasswordDto.class
        );

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                jwtRequestUsernamePasswordDto.getUsername(), jwtRequestUsernamePasswordDto.getPassword()
        );

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        List<String> authorities = authResult.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        String createAccessToken = JWT.create()
                .withSubject(authResult.getName())
                .withClaim("authorities", authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtSigning.getTokenExpired() * 3600)) //1 hour
                .withIssuer(request.getRequestURI())
                .sign(Algorithm.HMAC256(jwtSigning.getSecretKey().getBytes()));

        Map<String, Object> responseToken = new HashMap<>();
        responseToken.put("accessToken", createAccessToken);
        responseToken.put("tokenType", "Bearer");
        responseToken.put("permissions", authorities.stream().filter(i -> !i.startsWith("ROLE_")).collect(Collectors.toSet()));
        responseToken.put("username", authResult.getName());
        responseToken.put("roles", authorities.stream().filter(i -> i.startsWith("ROLE_")).collect(Collectors.toSet()));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), responseToken);
    }
}

package com.anhdungpham.spring_boot_security_backend.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
public class JwtOncePerRequestFilter extends OncePerRequestFilter {
    private final JwtSigning jwtSigning;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/auth/login") ||
                request.getServletPath().equals("/api/auth/refresh_token")) {

            filterChain.doFilter(request, response);
        } else {
            String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                try {
                    String token = tokenHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(jwtSigning.getSecretKey().getBytes());

                    //Verify token
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();

                    // Decode token
                    DecodedJWT decodedJWT = jwtVerifier.verify(token);

                    // get Username, authorities
                    String username = decodedJWT.getSubject();
                    Collection<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
                    String[] roles = decodedJWT.getClaim("authorities").asArray(String.class);
                    Arrays.stream(roles).forEach(
                            role -> grantedAuthorities.add(new SimpleGrantedAuthority(role))
                    );

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    username, null, grantedAuthorities);

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.error("error_message {}", e.getMessage());
                    response.setHeader("error_message", e.getMessage());
                    Map<String, String> error = new HashMap<>();

                    error.put("error_message", e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                throw new RuntimeException("ACCESS TOKEN IS WRONG !!!");
            }
        }
    }
}


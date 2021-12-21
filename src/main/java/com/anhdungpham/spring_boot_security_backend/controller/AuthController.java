package com.anhdungpham.spring_boot_security_backend.controller;

import com.anhdungpham.spring_boot_security_backend.entities.AuthorityEntity;
import com.anhdungpham.spring_boot_security_backend.entities.RoleEntity;
import com.anhdungpham.spring_boot_security_backend.entities.UserEntity;
import com.anhdungpham.spring_boot_security_backend.jwt.JwtSigning;
import com.anhdungpham.spring_boot_security_backend.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtSigning jwtSigning;
    private final UserRepository userRepository;

    @GetMapping("/refresh_token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Authorize as the token
        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            try {
                String oldToken = tokenHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(jwtSigning.getSecretKey().getBytes());

                //Verify token
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();

                // Decode token
                DecodedJWT decodedJWT = jwtVerifier.verify(oldToken);

                // get Username, authorities
                String username = decodedJWT.getSubject();

                UserEntity userEntity = userRepository.findByUsername(username);
                List<String> newAuthorityList = new ArrayList<>();

                for (RoleEntity i : userEntity.getRoleEntitySet()) {
                    newAuthorityList.add(i.getRoleName());
                    for (AuthorityEntity j : i.getAuthorityEntitySet()) {
                        newAuthorityList.add(j.getAuthorityName());
                    }
                }

                String newToken = JWT.create().withSubject(userEntity.getUsername())
                        .withClaim("authorities", newAuthorityList)
                        .withIssuer(request.getRequestURI())

                        .withExpiresAt(new Date(System.currentTimeMillis() + jwtSigning.getTokenExpired() * 3600))
                        .sign(Algorithm.HMAC256(jwtSigning.getSecretKey().getBytes()));

                Map<String, Object> refreshToken = new HashMap<>();
                refreshToken.put("accessToken", newToken);
                refreshToken.put("tokenType", "Bearer");
                refreshToken.put("permissions", newAuthorityList.stream().filter(i -> !i.startsWith("ROLE_")).collect(Collectors.toSet()));
                refreshToken.put("username", userEntity.getUsername());
                refreshToken.put("roles", newAuthorityList.stream().filter(i -> i.startsWith("ROLE_")).collect(Collectors.toSet()));
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), refreshToken);
            } catch (Exception e) {
                log.error("error_message {}", e.getMessage());
                response.setHeader("error_message", e.getMessage());
                Map<String, String> error = new HashMap<>();
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }
        }
    }
}

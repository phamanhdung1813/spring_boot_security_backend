package com.anhdungpham.spring_boot_security_backend.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class JwtSigning {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.tokenExpired}")
    private Integer tokenExpired;
}

package com.anhdungpham.spring_boot_security_backend.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtRequestUsernamePasswordDto {
    private String username;
    private String password;
}

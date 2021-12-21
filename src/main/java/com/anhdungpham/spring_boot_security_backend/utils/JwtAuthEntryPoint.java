package com.anhdungpham.spring_boot_security_backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("entry_point_error {}", authException.getMessage());
        Map<String, String> entryPointErrors = new HashMap<>();
        entryPointErrors.put("entry_point_error", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), entryPointErrors);
    }
}

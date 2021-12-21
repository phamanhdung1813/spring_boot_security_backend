package com.anhdungpham.spring_boot_security_backend.auth;

import com.anhdungpham.spring_boot_security_backend.entities.UserEntity;
import com.anhdungpham.spring_boot_security_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(String.format("USER %s NOT FOUND", username));
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return new CustomUserDetails(userEntity);
    }
}

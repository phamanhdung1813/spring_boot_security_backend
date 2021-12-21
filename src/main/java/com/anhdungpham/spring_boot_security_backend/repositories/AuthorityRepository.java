package com.anhdungpham.spring_boot_security_backend.repositories;

import com.anhdungpham.spring_boot_security_backend.entities.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
    AuthorityEntity findByAuthorityName(String authorityName);
}

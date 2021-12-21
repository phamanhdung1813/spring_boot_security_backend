package com.anhdungpham.spring_boot_security_backend.repositories;

import com.anhdungpham.spring_boot_security_backend.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRoleName(String roleName);
}

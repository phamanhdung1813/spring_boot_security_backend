package com.anhdungpham.spring_boot_security_backend.repositories;

import com.anhdungpham.spring_boot_security_backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}

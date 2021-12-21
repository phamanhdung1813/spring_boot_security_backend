package com.anhdungpham.spring_boot_security_backend.repositories;

import com.anhdungpham.spring_boot_security_backend.entities.ResourceEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
}

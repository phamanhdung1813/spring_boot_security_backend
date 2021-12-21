package com.anhdungpham.spring_boot_security_backend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Id;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

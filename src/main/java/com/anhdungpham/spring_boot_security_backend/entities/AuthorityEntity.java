package com.anhdungpham.spring_boot_security_backend.entities;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthorityEntity extends BaseEntity {

    private String authorityName;
}

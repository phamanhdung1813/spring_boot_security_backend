package com.anhdungpham.spring_boot_security_backend.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_entity", uniqueConstraints = {
        @UniqueConstraint(name = "user_unique_username", columnNames = "username"),
        @UniqueConstraint(name = "user_unique_email", columnNames = "email")
})
public class UserEntity extends BaseEntity {

    @Column(name = "username", nullable = false, columnDefinition = "varchar(100) default 'need value'")
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(100) default 'need value'")
    private String password;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(100) default 'need value'")
    private String email;

    @ManyToMany(targetEntity = RoleEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(foreignKey = @ForeignKey(name="fk_link_user_id"), inverseForeignKey = @ForeignKey(name="fk_link_role1_id"))
    private Set<RoleEntity> roleEntitySet = new HashSet<>();



}

package com.anhdungpham.spring_boot_security_backend.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleEntity extends BaseEntity {

    private String roleName;

    @ManyToMany(targetEntity = AuthorityEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(foreignKey = @ForeignKey(name="fk_link_role2_id"), inverseForeignKey = @ForeignKey(name="fk_link_authority_id"))
    private Set<AuthorityEntity> authorityEntitySet = new HashSet<>();

}

package com.anhdungpham.spring_boot_security_backend.services.imp;

import com.anhdungpham.spring_boot_security_backend.entities.AuthorityEntity;
import com.anhdungpham.spring_boot_security_backend.entities.RoleEntity;
import com.anhdungpham.spring_boot_security_backend.entities.UserEntity;

public interface IDBService {
    UserEntity addUser(UserEntity username);

    RoleEntity addRole(RoleEntity rolename);

    AuthorityEntity addAuthority(AuthorityEntity authorityName);

    void addRoleToUser(String username, String rolename);

    void addAuthorityToRole(String rolename, String authorityName);
}

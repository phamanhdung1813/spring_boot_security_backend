package com.anhdungpham.spring_boot_security_backend.services;

import com.anhdungpham.spring_boot_security_backend.entities.AuthorityEntity;
import com.anhdungpham.spring_boot_security_backend.entities.RoleEntity;
import com.anhdungpham.spring_boot_security_backend.entities.UserEntity;
import com.anhdungpham.spring_boot_security_backend.repositories.AuthorityRepository;
import com.anhdungpham.spring_boot_security_backend.repositories.RoleRepository;
import com.anhdungpham.spring_boot_security_backend.repositories.UserRepository;
import com.anhdungpham.spring_boot_security_backend.services.imp.IDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DBService implements IDBService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public UserEntity addUser(UserEntity username) {
        return userRepository.save(username);
    }

    @Override
    public RoleEntity addRole(RoleEntity rolename) {
        return roleRepository.save(rolename);
    }

    @Override
    public AuthorityEntity addAuthority(AuthorityEntity authorityName) {
        return authorityRepository.save(authorityName);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        UserEntity userEntity = userRepository.findByUsername(username);
        RoleEntity roleEntity = roleRepository.findByRoleName(rolename);
        userEntity.getRoleEntitySet().add(roleEntity);
    }

    @Override
    public void addAuthorityToRole(String rolename, String authorityName) {
        RoleEntity roleEntity = roleRepository.findByRoleName(rolename);
        AuthorityEntity authorityEntity = authorityRepository.findByAuthorityName(authorityName);
        roleEntity.getAuthorityEntitySet().add(authorityEntity);
    }
}

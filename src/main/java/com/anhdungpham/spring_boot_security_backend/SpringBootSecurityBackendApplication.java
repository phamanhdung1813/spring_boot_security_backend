package com.anhdungpham.spring_boot_security_backend;

import com.anhdungpham.spring_boot_security_backend.entities.AuthorityEntity;
import com.anhdungpham.spring_boot_security_backend.entities.RoleEntity;
import com.anhdungpham.spring_boot_security_backend.entities.UserEntity;
import com.anhdungpham.spring_boot_security_backend.services.DBService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

@SpringBootApplication
public class SpringBootSecurityBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeDefaultDb(DBService dbService) {
        return args -> {
            dbService.addUser(new UserEntity("admin", "admin", "admin@gmail.com", new HashSet<>()));
            dbService.addUser(new UserEntity("subadmin", "subadmin", "subadmin@gmail.com", new HashSet<>()));
            dbService.addUser(new UserEntity("user", "user", "user@gmail.com", new HashSet<>()));

            dbService.addRole(new RoleEntity("ROLE_ADMIN", new HashSet<>()));
            dbService.addRole(new RoleEntity("ROLE_MANAGER", new HashSet<>()));
            dbService.addRole(new RoleEntity("ROLE_SUBADMIN", new HashSet<>()));
            dbService.addRole(new RoleEntity("ROLE_USER", new HashSet<>()));

            dbService.addAuthority(new AuthorityEntity("ADMIN:READ"));
            dbService.addAuthority(new AuthorityEntity("ADMIN:WRITE"));
            dbService.addAuthority(new AuthorityEntity("USER:READ"));
            dbService.addAuthority(new AuthorityEntity("USER:WRITE"));

            dbService.addRoleToUser("admin", "ROLE_ADMIN");

            dbService.addRoleToUser("subadmin", "ROLE_MANAGER");
            dbService.addRoleToUser("subadmin", "ROLE_SUBADMIN");

            dbService.addRoleToUser("user", "ROLE_USER");

            dbService.addAuthorityToRole("ROLE_ADMIN", "ADMIN:READ");
            dbService.addAuthorityToRole("ROLE_ADMIN", "ADMIN:WRITE");
            dbService.addAuthorityToRole("ROLE_ADMIN", "USER:READ");
            dbService.addAuthorityToRole("ROLE_ADMIN", "USER:WRITE");

            dbService.addAuthorityToRole("ROLE_SUBADMIN", "ADMIN:READ");
            dbService.addAuthorityToRole("ROLE_SUBADMIN", "USER:READ");
            dbService.addAuthorityToRole("ROLE_MANAGER", "USER:WRITE");

            dbService.addAuthorityToRole("ROLE_USER", "USER:READ");
        };
    }
}

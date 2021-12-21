package com.anhdungpham.spring_boot_security_backend.controller;

import com.anhdungpham.spring_boot_security_backend.dto.ResourceDTO;
import com.anhdungpham.spring_boot_security_backend.dto.ResponseJsonAPI;
import com.anhdungpham.spring_boot_security_backend.entities.AuthorityEntity;
import com.anhdungpham.spring_boot_security_backend.entities.RoleEntity;
import com.anhdungpham.spring_boot_security_backend.entities.UserEntity;
import com.anhdungpham.spring_boot_security_backend.repositories.ResourceRepository;
import com.anhdungpham.spring_boot_security_backend.repositories.UserRepository;
import com.anhdungpham.spring_boot_security_backend.services.ResourceService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.anhdungpham.spring_boot_security_backend.entities.ResourceEntity;
import com.anhdungpham.spring_boot_security_backend.jwt.JwtSigning;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v1/resources")
@RequiredArgsConstructor
@Slf4j
public class ResourceController {
    private final ResourceRepository resourceRepository;
    private final ResponseJsonAPI responseJsonAPI;

    @GetMapping
    public String backend() {
        return "BACKEND API APPLICATION, USING POSTMAN OR REACTJS TO TEST SPRING SECURITY AND DATABASE";
    }

    @GetMapping(value = "/all_data")
    @PreAuthorize("hasAnyAuthority('ADMIN:READ','USER:READ')")
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam(name = "fieldName", required = false) String fieldName,
                                                      @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                      @RequestParam(name = "limit", defaultValue = "3") int limit) {

        return new ResponseEntity<>(responseJsonAPI.responseJson(offset, limit, fieldName), HttpStatus.OK);
    }

    @GetMapping(value = "/all_data/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN:READ','USER:READ')")
    public ResponseEntity<ResourceEntity> getById(@PathVariable Long id) {
        ResourceEntity resourceEntity = resourceRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("NOT FOUND NEWS with ID: %s", id))
        );
        return ResponseEntity.ok(resourceEntity);
    }

    @PostMapping(value = "/post_data")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResourceEntity> postNews(@RequestBody ResourceDTO resourceDTO) {
        ModelMapper modelMapper = new ModelMapper();
        ResourceEntity resourceEntity = this.resourceRepository.save(
                modelMapper.map(resourceDTO, ResourceEntity.class)
        );
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(resourceEntity.getId()).toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return ResponseEntity.created(uri).body(resourceEntity);
    }

    @PutMapping(value = "/update_data/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResourceEntity updateNews(@RequestBody ResourceDTO resourceDTO,
                                     @PathVariable Long id) {
        Optional<ResourceEntity> oldNew = this.resourceRepository.findById(id);
        if (oldNew.isEmpty()) {
            throw new IllegalStateException(String.format("NOT FOUND NEWS with ID: %s", id));
        }
        ModelMapper modelMapper = new ModelMapper();
        ResourceEntity resourceEntity = modelMapper.map(resourceDTO, ResourceEntity.class);
        resourceEntity.setId(id);
        return resourceRepository.save(resourceEntity);
    }

    @DeleteMapping(value = "/delete_data/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteNews(@PathVariable Long id) {
        Optional<ResourceEntity> deleteNew = this.resourceRepository.findById(id);
        if (deleteNew.isEmpty()) {
            throw new IllegalStateException(String.format("NOT FOUND NEWS WITH ID: %s", id));
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        this.resourceRepository.deleteById(id);
    }
}

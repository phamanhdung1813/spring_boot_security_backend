package com.anhdungpham.spring_boot_security_backend.services;

import com.anhdungpham.spring_boot_security_backend.entities.ResourceEntity;
import com.anhdungpham.spring_boot_security_backend.repositories.ResourceRepository;
import com.anhdungpham.spring_boot_security_backend.services.imp.IResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceService implements IResourceService {
    private final ResourceRepository resourceRepository;

    @Override
    public Page<ResourceEntity> findAndPagination(int offset, int limit, String fieldName) {
        return resourceRepository.findAll(PageRequest.of(offset, limit).withSort(Sort.by(Sort.Direction.ASC, fieldName)));
    }

    @Override
    public Page<ResourceEntity> findAll(Pageable pageable) {
        return resourceRepository.findAll(pageable);
    }
}

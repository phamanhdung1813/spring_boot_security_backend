package com.anhdungpham.spring_boot_security_backend.services.imp;

import com.anhdungpham.spring_boot_security_backend.entities.ResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IResourceService {
    Page<ResourceEntity> findAndPagination(int offset, int limit, String fieldName);
    Page<ResourceEntity> findAll(Pageable pageable);

}

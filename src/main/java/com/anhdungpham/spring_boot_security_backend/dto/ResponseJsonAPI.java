package com.anhdungpham.spring_boot_security_backend.dto;

import com.anhdungpham.spring_boot_security_backend.entities.ResourceEntity;
import com.anhdungpham.spring_boot_security_backend.repositories.ResourceRepository;
import com.anhdungpham.spring_boot_security_backend.services.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ResponseJsonAPI {
    private final ResourceService resourceService;

    public Map<String, Object> responseJson(int offset, int limit, String fieldName) {
        Map<String, Object> response = new HashMap<>();
        if (fieldName == null) {
            Page<ResourceEntity> allResource = resourceService.findAll(PageRequest.of(offset, limit));
            response.put("totalElement", allResource.getTotalElements());
            response.put("responseData", allResource.getContent());
            response.put("totalPage", allResource.getTotalPages());
            response.put("currentPage", allResource.getNumber());
        } else {
            Page<ResourceEntity> allResourcePaging = resourceService.findAndPagination(offset, limit, fieldName);
            response.put("totalElement", allResourcePaging.getTotalElements());
            response.put("responseData", allResourcePaging.getContent());
            response.put("totalPage", allResourcePaging.getTotalPages());
            response.put("currentPage", allResourcePaging.getNumber());
        }
        return response;
    }
}

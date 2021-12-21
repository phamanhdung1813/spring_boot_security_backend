package com.anhdungpham.spring_boot_security_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {

    private String name;
    private int price;
    private String shortDescription;
    private int quantity;
}

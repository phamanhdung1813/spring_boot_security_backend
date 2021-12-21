package com.anhdungpham.spring_boot_security_backend.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="resource_data")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class ResourceEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "quantity")
    private int quantity;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date modifiedDate;


    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}

package com.happydieting.dev.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SYSTEM_CONFIG")
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemConfigModel extends ItemModel{

    @Column(name = "config_key", nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String value;

    @Column(length = 500)
    private String description;
}

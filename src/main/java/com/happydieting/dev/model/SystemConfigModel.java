package com.happydieting.dev.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SYSTEM_CONFIG")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
public class SystemConfigModel extends ItemModel{

    @Column(name = "config_key", nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String value;

    @Column(length = 500)
    private String description;
}

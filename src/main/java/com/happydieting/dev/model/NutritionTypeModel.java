package com.happydieting.dev.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "NUTRITION_TYPE")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
public class NutritionTypeModel extends ItemModel{
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
}

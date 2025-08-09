package com.happydieting.dev.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "NUTRITION_UNIT")
@EqualsAndHashCode(callSuper = true)
@Data
public class NutritionUnitModel extends ItemModel{
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
}

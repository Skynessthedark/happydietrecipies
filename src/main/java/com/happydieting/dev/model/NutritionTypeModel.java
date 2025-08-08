package com.happydieting.dev.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "NUTRITION_TYPE")
@EqualsAndHashCode(callSuper = false)
@Data
public class NutritionTypeModel extends ItemModel{
    private String code;
    private String name;
}

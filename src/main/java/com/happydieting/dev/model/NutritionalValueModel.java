package com.happydieting.dev.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "NUTRITIONAL_VALUES", uniqueConstraints = { @UniqueConstraint(columnNames = { "recipe", "type" }) })
public class NutritionalValueModel extends ItemModel{

    @Column(nullable = false)
    private Double value;

    @ManyToOne
    @JoinColumn(nullable = false)
    private NutritionTypeModel type;

    @ManyToOne
    @JoinColumn(nullable = false)
    private NutritionUnitModel unit;

    @ManyToOne
    @JoinColumn(nullable = false)
    private RecipeModel recipe;
}

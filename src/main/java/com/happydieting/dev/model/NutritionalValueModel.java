package com.happydieting.dev.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "NUTRITIONAL_VALUES", uniqueConstraints = { @UniqueConstraint(columnNames = { "recipe_id", "type_id" }) })
public class NutritionalValueModel extends ItemModel{

    @Column(nullable = false)
    private Double value;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private NutritionTypeModel type;

    @ManyToOne
    @JoinColumn //(nullable = false)
    private NutritionUnitModel unit;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private RecipeModel recipe;
}

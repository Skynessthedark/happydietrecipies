package com.happydieting.dev.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "NUTRITIONAL_VALUES")
@EqualsAndHashCode(callSuper = false)
@Data
public class NutritionalValueModel extends ItemModel{

    private Double value;

    @ManyToOne
    @JoinColumn
    private NutritionTypeModel type;

    @ManyToOne
    @JoinColumn
    private NutritionUnitModel unit;

    @ManyToOne
    @JoinColumn
    private RecipeModel recipe;
}

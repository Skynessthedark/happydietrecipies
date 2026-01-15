package com.happydieting.dev.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeData extends ItemData {
    private String code;
    @NotBlank(message = "Name cannot be empty.")
    private String name;
    @NotBlank(message = "Description cannot be empty.")
    private String description;
    @NotBlank(message = "Recipe cannot be empty.")
    private String recipe;
    //private byte[] image;
    private String tips;
    @NotNull(message = "Serving Amount cannot be empty.")
    private double servingAmount;
    private UserData owner;
    private NutritionUnitData servingUnit;
    private List<NutritionalValueData> nutritionalValues;
    private List<IngredientData> ingredients;
    private List<CategoryData> categories;
}

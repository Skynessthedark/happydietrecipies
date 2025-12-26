package com.happydieting.dev.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeData extends ItemData {
    private String code;
    private String name;
    private String description;
    private String recipe;
    //private byte[] image;
    private String tips;
    private double servingAmount;
    private UserData owner;
    private NutritionUnitData servingUnit;
    private List<NutritionalValueData> nutritionalValues;
    private List<IngredientData> ingredients;
    private List<CategoryData> categories;
}

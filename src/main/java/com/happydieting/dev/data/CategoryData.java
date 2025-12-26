package com.happydieting.dev.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryData extends ItemData {
    private String code;
    private String name;
    private String description;
    private List<RecipeData> recipes;
    private List<IngredientData> ingredients;
    private List<CategoryData> subcategories;
    private List<CategoryData> parentCategories;
}

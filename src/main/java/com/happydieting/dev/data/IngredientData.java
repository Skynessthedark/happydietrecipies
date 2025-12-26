package com.happydieting.dev.data;

import com.happydieting.dev.model.CategoryModel;
import com.happydieting.dev.model.IngredientModel;
import com.happydieting.dev.model.NutritionUnitModel;
import com.happydieting.dev.model.NutritionalValueModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class IngredientData extends ItemData {
    private String code;
    private String name;
    private List<CategoryData> categories;

    public IngredientData(String code, String name) {
        this.code = code;
        this.name = name;
    }
}

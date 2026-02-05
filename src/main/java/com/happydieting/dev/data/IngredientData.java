package com.happydieting.dev.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

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

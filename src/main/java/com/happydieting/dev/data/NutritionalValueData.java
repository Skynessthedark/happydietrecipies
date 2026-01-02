package com.happydieting.dev.data;

import lombok.Data;

@Data
public class NutritionalValueData {

    private String id;
    private Double value;
    private GenericData nutritionType;
    private GenericData nutritionUnit;
    private String recipeId;
}

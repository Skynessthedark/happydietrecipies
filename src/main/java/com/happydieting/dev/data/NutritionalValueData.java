package com.happydieting.dev.data;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class NutritionalValueData {

    private String id;
    @NonNull
    private Double value;
    @NonNull
    private GenericData nutritionType;
    @NonNull
    private GenericData nutritionUnit;
    @NonNull
    private String recipeId;
}

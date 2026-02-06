package com.happydieting.dev.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class NutritionalValueData implements Serializable {

    private long id;
    private Double value;
    private GenericData nutritionType;
    private GenericData nutritionUnit;
    private String recipeCode;
}

package com.happydieting.dev.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class NutritionUnitData extends ItemData {
    private String code;
    private String name;
}

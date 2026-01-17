package com.happydieting.dev.util;

import org.apache.commons.lang3.RandomStringUtils;

public class RecipeUtil {

    private RecipeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateRecipeCode(String name) {
        return name.replaceAll("\\s", "").toLowerCase() + RandomStringUtils.secure().nextAlphanumeric(10);
    }
}

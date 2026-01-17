package com.happydieting.dev.enums;

public enum RecipeMediaPath {
    RECIPE_IMAGE_NAME("%s-image"),
    RECIPE_IMAGE_URL("/recipes/%s/image");

    private final String pattern;

    RecipeMediaPath(String pattern) {
        this.pattern = pattern;
    }

    public String resolve(Object... args) {
        return String.format(pattern, args);
    }
}

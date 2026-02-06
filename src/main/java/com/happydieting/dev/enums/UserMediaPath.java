package com.happydieting.dev.enums;

public enum UserMediaPath {
    IMAGE_NAME("%s-image"),
    IMAGE_URL("/user/%s/image");

    private final String pattern;

    UserMediaPath(String pattern) {
        this.pattern = pattern;
    }

    public String resolve(Object... args) {
        return String.format(pattern, args);
    }
}

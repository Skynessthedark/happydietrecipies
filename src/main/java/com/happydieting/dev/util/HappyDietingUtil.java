package com.happydieting.dev.util;

import java.util.UUID;

public class HappyDietingUtil {

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}

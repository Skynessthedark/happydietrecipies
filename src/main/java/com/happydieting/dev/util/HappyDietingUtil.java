package com.happydieting.dev.util;

import com.happydieting.dev.data.response.ResponseData;

import java.util.UUID;

public class HappyDietingUtil {

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static ResponseData generateResponse(boolean status, String message) {
        return new ResponseData(status, message);
    }
}

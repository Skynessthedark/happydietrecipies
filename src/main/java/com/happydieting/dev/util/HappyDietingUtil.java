package com.happydieting.dev.util;

import com.happydieting.dev.data.response.ResponseData;

public class HappyDietingUtil {

    private HappyDietingUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static ResponseData generateResponse(boolean status, String message) {
        return new ResponseData(status, message);
    }
}

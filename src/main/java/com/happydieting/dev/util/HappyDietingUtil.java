package com.happydieting.dev.util;

import com.happydieting.dev.data.ItemData;
import com.happydieting.dev.data.response.ResponseData;
import com.happydieting.dev.model.ItemModel;

public class HappyDietingUtil {

    private HappyDietingUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static ResponseData generateResponse(boolean status, String message) {
        return new ResponseData(status, message);
    }

    public static <T extends ItemModel, S extends ItemData> void convertItemToData(T model, S data) {
        data.setId(model.getId());
        data.setCreatedDate(DateUtil.convertDateToString(model.getCreatedDate()));
        data.setModifiedDate(DateUtil.convertDateToString(model.getModifiedDate()));
        data.setEnabled(model.getEnabled());
    }
}

package com.happydieting.dev.data.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseData implements Serializable {
    private String message;
    private boolean status;

    public ResponseData() {
    }

    public ResponseData(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}

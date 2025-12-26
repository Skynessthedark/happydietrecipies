package com.happydieting.dev.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class ItemData implements Serializable {
    private Long id;
    private String createdDate;
    private String modifiedDate;
    private boolean enabled;
}

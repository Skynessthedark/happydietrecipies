package com.happydieting.dev.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class GenericData implements Serializable {
    private Long id;
    private String name;
    private String code;

    public GenericData(String code) {
        this.code = code;
    }

    public GenericData(String name, String code) {
        this.name = name;
        this.code = code;
    }
}

package com.happydieting.dev.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericData {
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

package com.happydieting.dev.data;

import lombok.Data;

@Data
public class UserData extends ItemData {
    private String username;
    private String fullName;
    private String email;
    private String password;
    //private byte[] avatar;
    private String bio;
}

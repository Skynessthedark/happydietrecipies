package com.happydieting.dev.data;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserData extends ItemData {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String imageUrl;
    private transient MultipartFile image;
    private String bio;
}

package com.happydieting.dev.data;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterData{
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String bio;
    private MultipartFile recipeFile;

    public RegisterData(String bio, String username, String fullName, String email, String password) {
        this.bio = bio;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}

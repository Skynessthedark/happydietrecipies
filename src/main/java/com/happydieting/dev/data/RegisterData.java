package com.happydieting.dev.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class RegisterData implements Serializable {
    @NotBlank(message = "Full name cannot be empty.")
    private String fullName;
    @NotBlank(message = "Email cannot be empty.")
    private String email;
    @NotBlank(message = "Password cannot be empty.")
    private String password;
    private transient MultipartFile image;
    private String bio;

}

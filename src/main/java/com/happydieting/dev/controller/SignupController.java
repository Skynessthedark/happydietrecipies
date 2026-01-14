package com.happydieting.dev.controller;

import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig'deki bean'i kullanıyoruz

    public SignupController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getSignupPage(Model model) {
        model.addAttribute("user", new UserModel());
        return "signup";
    }


    @PostMapping
    public String register(UserModel user,
                           @RequestParam("rePassword") String rePassword,
                           Model model) {

        if (!user.getPassword().equals(rePassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "signup";
        }
        user.setBio(user.getBio());
        user.setFullName(user.getFullName());

        user.setUsername(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return "redirect:/login";
    }
}
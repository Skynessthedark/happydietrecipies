package com.happydieting.dev.controller;


import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private UserRepository userRepository;

    public SignupController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getSignupPage(Model model) {
        model.addAttribute("signup", new RecipeData());
        model.addAttribute("servingUnits", userRepository.findAll());
        return "signup";
    }
}

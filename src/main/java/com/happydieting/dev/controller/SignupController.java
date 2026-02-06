package com.happydieting.dev.controller;

import com.happydieting.dev.data.RegisterData;
import com.happydieting.dev.repository.UserRepository;
import com.happydieting.dev.security.service.AutoLoginService;
import com.happydieting.dev.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;
    private final AutoLoginService autoLoginService;

    public SignupController(UserService userService, AutoLoginService autoLoginService) {
        this.userService = userService;
        this.autoLoginService = autoLoginService;
    }

    @GetMapping
    public String getSignupPage(Model model) {
        model.addAttribute("registerForm", new RegisterData());
        return "signup";
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String register(
            @Valid @ModelAttribute("registerForm") RegisterData registerForm,
            @RequestParam("rePassword") String rePassword,
            @RequestParam(value = "image", required = false) MultipartFile image,
            BindingResult bindingResult, Model model) {

        if (registerForm != null && !registerForm.getPassword().equals(rePassword)) {
            bindingResult.rejectValue("password", "error.password", "Passwords do not match.");
        }

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        boolean isCreated = userService.createUser(registerForm, image);
        if (isCreated) {
            autoLoginService.autoLogin(registerForm.getEmail(), registerForm.getPassword());
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", "An error occured while registering...");
            return "signup";
        }
    }
}

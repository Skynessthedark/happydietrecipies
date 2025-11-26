package com.happydieting.dev.controller;

import com.happydieting.dev.security.service.CustomUserDetailsService;
import com.happydieting.dev.security.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;


    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String logout,
                               Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "An error occured during login. Please try again.");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "You have been successfully logged out.");
        }
        return "login";
    }

}

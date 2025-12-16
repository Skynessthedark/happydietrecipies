package com.happydieting.dev.controller;

import com.happydieting.dev.data.AuthData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

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

        model.addAttribute("loginForm", new AuthData());
        return "login";
    }

}

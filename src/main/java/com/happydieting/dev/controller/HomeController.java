package com.happydieting.dev.controller;

import com.happydieting.dev.constant.ControllerConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(ControllerConstant.HOME)
    public String getHome() {
        return "home";
    }
}

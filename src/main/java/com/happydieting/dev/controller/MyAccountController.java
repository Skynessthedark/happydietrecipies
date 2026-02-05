package com.happydieting.dev.controller;

import com.happydieting.dev.facade.RecipeFacade;
import com.happydieting.dev.security.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/me")
public class MyAccountController {

    private final SessionService sessionService;
    private final RecipeFacade recipeFacade;

    public MyAccountController(SessionService sessionService, RecipeFacade recipeFacade) {
        this.sessionService = sessionService;
        this.recipeFacade = recipeFacade;
    }

    @GetMapping("/recipes")
    public String getMyRecipesPage(Model model) {
        model.addAttribute("recipes", recipeFacade.getRecipesOfUser(sessionService.getSessionUser()));
        return "my-account/recipes";
    }
}

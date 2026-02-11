package com.happydieting.dev.controller;

import com.happydieting.dev.constant.ControllerConstant;
import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.facade.RecipeFacade;
import com.happydieting.dev.security.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(ControllerConstant.MyAccount.MY_ACCOUNT)
public class MyAccountController {

    private final SessionService sessionService;
    private final RecipeFacade recipeFacade;

    public MyAccountController(SessionService sessionService, RecipeFacade recipeFacade) {
        this.sessionService = sessionService;
        this.recipeFacade = recipeFacade;
    }

    @GetMapping(ControllerConstant.RECIPES)
    public String getMyRecipesPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "9") int size,
                                   @RequestParam(required = false) String sort,
                                   Model model) {
        List<RecipeData> recipes;
        if (sort != null && !sort.isBlank()) {
            recipes = recipeFacade.getRecipesOfUser(sessionService.getSessionUser(), page, size, sort);
        } else {
            recipes = recipeFacade.getRecipesOfUser(sessionService.getSessionUser(), page, size);
        }

        boolean hasPrev = page > 0;
        boolean hasNext = recipes != null && recipes.size() == size;

        model.addAttribute("recipes", recipes);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);

        return "my-account/recipes";
    }
}

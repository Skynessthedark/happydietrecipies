package com.happydieting.dev.controller;

import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.data.UserData;
import com.happydieting.dev.data.response.ResponseData;
import com.happydieting.dev.facade.RecipeFacade;
import com.happydieting.dev.security.service.SessionService;
import com.happydieting.dev.service.NutritionService;
import com.happydieting.dev.service.UserService;
import com.happydieting.dev.util.HappyDietingUtil;
import com.happydieting.dev.util.MediaUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final NutritionService nutritionService;
    private final SessionService sessionService;
    private final UserService userService;
    private final RecipeFacade recipeFacade;

    public RecipeController(NutritionService nutritionService, SessionService sessionService, UserService userService, RecipeFacade recipeFacade) {
        this.nutritionService = nutritionService;
        this.sessionService = sessionService;
        this.userService = userService;
        this.recipeFacade = recipeFacade;
    }

    @GetMapping
    public String getRecipeListPage(Model model) {
        return "recipe/list";
    }

    @GetMapping("/new")
    public String getRecipeAddPage(Model model) {
        model.addAttribute("recipeForm", new RecipeData());
        model.addAttribute("servingUnits", nutritionService.getAllNutritionUnits());
        model.addAttribute("nutritionalTypes", nutritionService.getAllNutritionTypes());
        return "recipe/new-recipe";
    }

    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseData addNewRecipe(@Valid @RequestPart("recipeForm") RecipeData recipeForm,
                                                     @RequestPart(value = "image", required = false) final MultipartFile image) {
        final UserData owner = userService.convertModel2Data(sessionService.getSessionUser());
        if(owner == null) return HappyDietingUtil.generateResponse(false, "No session user.");

        recipeForm.setOwner(owner);
        recipeForm.setImage(image);
        boolean isSaved = recipeFacade.create(recipeForm);
        return HappyDietingUtil.generateResponse(isSaved, "Recipe created successfully.");
    }

    @GetMapping(value = "/{recipeCode}/image")
    public void getRecipeImage(@PathVariable("recipeCode") String recipeCode, HttpServletResponse response) throws IOException {
        MediaUtil.printMedia(recipeFacade.getRecipeImage(recipeCode), response);
    }

    @GetMapping(value = "/{recipeCode}")
    public String getRecipe(@PathVariable("recipeCode") String recipeCode, Model model) {
        //TODO: redirection ve global exception handler tanımlanacak.
        if(recipeCode == null) return "redirect:/recipes";

        model.addAttribute("recipe", recipeFacade.getRecipeByCode(recipeCode));

        return "recipe/recipe-detail";
    }
}

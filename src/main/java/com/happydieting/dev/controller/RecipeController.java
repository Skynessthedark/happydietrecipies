package com.happydieting.dev.controller;

import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.data.UserData;
import com.happydieting.dev.data.response.ResponseData;
import com.happydieting.dev.repository.NutritionTypeRepository;
import com.happydieting.dev.repository.NutritionUnitRepository;
import com.happydieting.dev.security.service.SessionService;
import com.happydieting.dev.service.RecipeService;
import com.happydieting.dev.util.HappyDietingUtil;
import com.happydieting.dev.util.MediaUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    private final NutritionUnitRepository nutritionUnitRepository;
    private final NutritionTypeRepository nutritionTypeRepository;
    private final RecipeService recipeService;
    private final SessionService sessionService;

    public RecipeController(NutritionUnitRepository nutritionUnitRepository, NutritionTypeRepository nutritionTypeRepository, RecipeService recipeService, SessionService sessionService) {
        this.nutritionUnitRepository = nutritionUnitRepository;
        this.nutritionTypeRepository = nutritionTypeRepository;
        this.recipeService = recipeService;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String getRecipeListPage(Model model) {
        return "recipe/list";
    }

    @GetMapping("/new")
    public String getRecipeAddPage(Model model) {
        model.addAttribute("recipeForm", new RecipeData());
        model.addAttribute("servingUnits", nutritionUnitRepository.findNutritionUnits());
        model.addAttribute("nutritionalTypes", nutritionTypeRepository.findAll());
        return "recipe/new-recipe";
    }

    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseData addNewRecipe(@Valid @RequestPart("recipeForm") RecipeData recipeForm,
                                                     @RequestPart(value = "image", required = false) final MultipartFile image,
                                                     HttpServletRequest request) {
        final UserData owner = sessionService.getSessionUserData(request);
        if(owner == null) return HappyDietingUtil.generateResponse(false, "No session user.");

        recipeForm.setOwner(owner);
        recipeForm.setImage(image);
        boolean isSaved = recipeService.create(recipeForm);
        return HappyDietingUtil.generateResponse(isSaved, "Recipe created successfully.");
    }

    @GetMapping(value = "/{recipeCode}/image")
    public void getRecipeImage(@PathVariable("recipeCode") String recipeCode, HttpServletResponse response) throws IOException {
        MediaUtil.printMedia(recipeService.getRecipeImage(recipeCode), response);
    }
}

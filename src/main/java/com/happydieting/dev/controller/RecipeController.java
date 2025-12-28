package com.happydieting.dev.controller;

import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.model.NutritionTypeModel;
import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.repository.NutritionTypeRepository;
import com.happydieting.dev.repository.NutritionUnitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private NutritionUnitRepository nutritionUnitRepository;
    private NutritionTypeRepository nutritionTypeRepository;

    public RecipeController(NutritionUnitRepository nutritionUnitRepository, NutritionTypeRepository nutritionTypeRepository) {
        this.nutritionUnitRepository = nutritionUnitRepository;
        this.nutritionTypeRepository = nutritionTypeRepository;
    }

    @GetMapping
    public String getRecipeListPage(Model model) {
        return "recipe/list";
    }

    @GetMapping("/new")
    public String getRecipeAddPage(Model model) {
        model.addAttribute("recipe", new RecipeData());
        model.addAttribute("servingUnits", nutritionUnitRepository.findNutritionUnits());
        model.addAttribute("nutritionalTypes", nutritionTypeRepository.findAll());
        return "recipe/new-recipe";
    }
}

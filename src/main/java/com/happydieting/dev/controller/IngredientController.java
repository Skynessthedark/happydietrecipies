package com.happydieting.dev.controller;

import com.happydieting.dev.data.IngredientData;
import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.repository.IngredientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/search")
    @ResponseBody
    public List<IngredientData> searchSellers(@RequestParam String ingredientName) {
        return ingredientRepository.searchDataByName(ingredientName);
    }
}

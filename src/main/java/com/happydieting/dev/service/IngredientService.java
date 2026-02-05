package com.happydieting.dev.service;

import com.happydieting.dev.data.IngredientData;
import com.happydieting.dev.model.IngredientModel;
import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.repository.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IngredientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientData> getIngredientDatas(RecipeModel recipe) {
        return convertModelsToDatas(getIngredients(recipe));
    }

    public List<IngredientData> convertModelsToDatas(List<IngredientModel> ingredients) {
        if(ingredients == null || ingredients.isEmpty()) return Collections.emptyList();

        List<IngredientData> ingredientDatas = new ArrayList<>();
        for(IngredientModel ingredient: ingredients){
            ingredientDatas.add(convertModelToData(ingredient));
        }

        return ingredientDatas;
    }

    public IngredientData convertModelToData(IngredientModel ingredient) {
        IngredientData ingredientData = new IngredientData();
        ingredientData.setId(ingredient.getId());
        ingredientData.setCode(ingredient.getCode());
        ingredientData.setName(ingredient.getName());

        return ingredientData;
    }

    public List<IngredientModel> getIngredients(RecipeModel recipe) {
        if(recipe == null){
            LOGGER.error("Recipe is null.");
            return Collections.emptyList();
        }

        return ingredientRepository.findIngredientModelsByRecipe(recipe);
    }
}

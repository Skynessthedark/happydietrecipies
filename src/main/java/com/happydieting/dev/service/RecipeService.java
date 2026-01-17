package com.happydieting.dev.service;

import com.happydieting.dev.data.*;
import com.happydieting.dev.model.*;
import com.happydieting.dev.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final NutritionUnitRepository nutritionUnitRepository;
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final NutritionTypeRepository nutritionTypeRepository;
    private final UserRepository userRepository;
    private final NutritionalValueRepository nutritionalValueRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, NutritionUnitRepository nutritionUnitRepository, CategoryRepository categoryRepository, IngredientRepository ingredientRepository, NutritionTypeRepository nutritionTypeRepository, UserRepository userRepository, NutritionalValueRepository nutritionalValueRepository) {
        this.recipeRepository = recipeRepository;
        this.nutritionUnitRepository = nutritionUnitRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.nutritionTypeRepository = nutritionTypeRepository;
        this.userRepository = userRepository;
        this.nutritionalValueRepository = nutritionalValueRepository;
    }


    public boolean create(final RecipeData recipeForm) {
        //TODO: validation and logging required
        if(recipeForm == null || recipeForm.getServingUnit() == null
                || recipeForm.getIngredients() == null || recipeForm.getCategories() == null) return false;
        if(recipeForm.getId() != null || recipeForm.getCode() != null) return false;

        RecipeModel newRecipe = new RecipeModel();
        newRecipe.setName(recipeForm.getName());
        newRecipe.setDescription(recipeForm.getDescription());
        newRecipe.setRecipe(recipeForm.getRecipe());
        newRecipe.setTips(recipeForm.getTips());

        UserModel owner = userRepository.findByUsername(recipeForm.getOwner().getUsername()).orElse(null);
        if(owner == null) return false;
        newRecipe.setOwner(owner);

        newRecipe.setServingAmount(recipeForm.getServingAmount());
        NutritionUnitModel servingUnitModel = getServingUnitModel(recipeForm.getServingUnit());
        if(servingUnitModel == null) return false;
        newRecipe.setServingUnit(servingUnitModel);

        Set<CategoryModel> categoryModelSet = getCategoryModels(recipeForm.getCategories());
        if(CollectionUtils.isEmpty(categoryModelSet)) return false;
        newRecipe.setCategories(categoryModelSet);

        Set<IngredientModel> ingredientModelSet = getIngredientModels(recipeForm.getIngredients());
        if(CollectionUtils.isEmpty(ingredientModelSet)) return false;
        newRecipe.setIngredients(ingredientModelSet);

        //TODO: image upload required
        //private byte[] image;

        recipeRepository.save(newRecipe);

        createNutritionalValues(newRecipe, recipeForm.getNutritionalValues());

        return true;
    }

    private NutritionUnitModel getServingUnitModel(NutritionUnitData servingUnit) {
        if(servingUnit == null || servingUnit.getCode() == null) return null;
        return nutritionUnitRepository.findNutritionUnitModelByCode(servingUnit.getCode()).orElse(null);
    }

    private Set<CategoryModel> getCategoryModels(List<CategoryData> categories) {
        if(categories == null || categories.isEmpty()) return Collections.emptySet();

        return new ArrayList<>(categories).stream()
                .map(categoryData -> categoryRepository.findCategoryModelByCode(categoryData.getCode()).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private Set<IngredientModel> getIngredientModels(List<IngredientData> ingredients) {
        if(ingredients == null || ingredients.isEmpty()) return Collections.emptySet();

        return new ArrayList<>(ingredients).stream()
                .map(ingredientData -> ingredientRepository.findIngredientModelByCode(ingredientData.getCode()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void createNutritionalValues(RecipeModel recipe, List<NutritionalValueData> nutritionalValues) {
        if(nutritionalValues == null || nutritionalValues.isEmpty() || recipe == null) return;

        for (NutritionalValueData nutritionalValue : nutritionalValues) {
            if(validateNutritionalValue(nutritionalValue)){
                NutritionalValueModel nutritionalValueModel = new NutritionalValueModel();

                NutritionTypeModel nutritionTypeModel = nutritionTypeRepository.findNutritionTypeModelByCode(nutritionalValue.getNutritionType().getCode()).orElse(null);
                if(nutritionTypeModel == null) continue;

                nutritionalValueModel.setRecipe(recipe);
                nutritionalValueModel.setType(nutritionTypeModel);
                nutritionalValueModel.setValue(nutritionalValue.getValue());

                nutritionalValueRepository.save(nutritionalValueModel);
            }
        }
    }

    private boolean validateNutritionalValue(NutritionalValueData nutritionalValue) {
        boolean isNotValid = nutritionalValue == null || nutritionalValue.getValue() == null
                || nutritionalValue.getNutritionType() == null
                || nutritionalValue.getNutritionType().getCode() == null;
        return !isNotValid;
    }
}

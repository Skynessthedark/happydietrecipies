package com.happydieting.dev.facade;

import com.happydieting.dev.data.NutritionUnitData;
import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.exception.InvalidRecipeException;
import com.happydieting.dev.model.MediaModel;
import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.service.*;
import com.happydieting.dev.util.HappyDietingUtil;
import com.happydieting.dev.validator.RecipeFormValidator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class RecipeFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeFacade.class);

    private final RecipeService recipeService;
    private final RecipeFormValidator recipeFormValidator;
    private final UserService userService;
    private final MediaService mediaService;
    private final NutritionalValueService nutritionalValueService;
    private final ModelMapper modelMapper;
    private final IngredientService ingredientService;
    private final CategoryService categoryService;

    public RecipeFacade(RecipeService recipeService, RecipeFormValidator recipeFormValidator, UserService userService, MediaService mediaService, NutritionService nutritionService, NutritionalValueService nutritionalValueService, ModelMapper modelMapper, IngredientService ingredientService, CategoryService categoryService) {
        this.recipeService = recipeService;
        this.recipeFormValidator = recipeFormValidator;
        this.userService = userService;
        this.mediaService = mediaService;
        this.nutritionalValueService = nutritionalValueService;
        this.modelMapper = modelMapper;
        this.ingredientService = ingredientService;
        this.categoryService = categoryService;
    }


    public boolean create(final RecipeData recipeForm) {
        try{
            if(recipeFormValidator.isInvalid(recipeForm, Boolean.TRUE)){
                throw new InvalidRecipeException("Recipe Form is null or invalid.");
            }

            return recipeService.create(recipeForm);
        } catch (InvalidRecipeException e) {
            LOGGER.error("Recipe creation failed.", e);
        } catch (Exception e) {
            LOGGER.error("An exception occured.", e);
        }
        return false;
    }

    public List<RecipeData> getRecipesOfUser(UserModel user) {
        if(checkUserAndLog(user)) return getRecipeDataList(recipeService.getRecipeModels(user));

        return Collections.emptyList();
    }

    public List<RecipeData> getRecipesOfUser(UserModel owner, Integer page, Integer size) {
        if(checkPageAndSize(page, size) && checkUserAndLog(owner)) return getRecipeDataList(recipeService.getRecipeModels(owner, page, size));

        return Collections.emptyList();
    }

    public List<RecipeData> getRecipesOfUser(UserModel owner, Integer page, Integer size, String keyword) {
        if(checkPageAndSize(page, size)
                && checkUserAndLog(owner)
                && keyword != null && !keyword.isBlank()) return getRecipeDataList(recipeService.getRecipeModels(owner, page, size, keyword));

        return Collections.emptyList();
    }

    public MediaModel getRecipeImage(String recipeCode){
        return StringUtils.hasText(recipeCode)?
                recipeService.getRecipeImage(recipeCode):
                null;
    }

    public List<RecipeData> getRecipeDataList(List<RecipeModel> recipes) {
        if(recipes == null || recipes.isEmpty()) return Collections.emptyList();

        List<RecipeData> recipeList = new ArrayList<>();
        for(RecipeModel recipe: recipes){
            recipeList.add(getRecipeData(recipe));
        }
        return recipeList;
    }

    public RecipeData getRecipeData(RecipeModel recipe) {
        if(Objects.isNull(recipe)) return null;

        RecipeData recipeData = new RecipeData();
        HappyDietingUtil.convertItemToData(recipe, recipeData);
        recipeData.setCode(recipe.getCode());
        recipeData.setName(recipe.getName());
        recipeData.setRecipe(recipe.getRecipe());
        recipeData.setDescription(recipe.getDescription());
        recipeData.setTips(recipe.getTips());
        recipeData.setServingAmount(recipe.getServingAmount());

        recipeData.setOwner(userService.convertModel2Data(recipe.getOwner()));
        recipeData.setServingUnit(modelMapper.map(recipe.getServingUnit(), NutritionUnitData.class));
        recipeData.setNutritionalValues(nutritionalValueService.getNutritionalValueDatas(recipe));
        recipeData.setIngredients(ingredientService.getIngredientDatas(recipe));
        recipeData.setCategories(categoryService.getCategoryDatas(recipe));
        recipeData.setImageUrl(mediaService.getMediaUrlByOwner(recipe.getId(), RecipeModel.class));
        return recipeData;
    }

    public RecipeData getRecipeByCode(String recipeCode) {
        if(recipeCode == null) return null;
        RecipeModel recipe = recipeService.getRecipeByCode(recipeCode);
        return recipe.getEnabled().equals(Boolean.TRUE)? getRecipeData(recipe): null;
    }

    public List<RecipeData> getRecipes(Integer page, Integer size) {
        if(checkPageAndSize(page, size))
            return getRecipesAsDataList(recipeService.getRecipes(page, size));

        return Collections.emptyList();
    }

    public List<RecipeData> getRecipes(Integer page, Integer size, String keyword) {
        if(checkPageAndSize(page, size)
                || keyword == null || keyword.isBlank())
            return getRecipesAsDataList(recipeService.getRecipes(page, size, keyword));

        return Collections.emptyList();
    }

    private List<RecipeData> getRecipesAsDataList(List<RecipeModel> recipes) {
        if(recipes == null || recipes.isEmpty()) return Collections.emptyList();

        return getRecipeDataList(recipes);
    }

    private boolean checkPageAndSize(Integer page, Integer size) {
        return page != null && page >= 0
                && size != null && size >= 0;
    }

    private boolean checkUserAndLog(UserModel user) {
        if(user == null){
            LOGGER.error("User is not found.");
            return false;
        }
        return true;
    }
}

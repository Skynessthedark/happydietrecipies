package com.happydieting.dev.service;

import com.happydieting.dev.data.CategoryData;
import com.happydieting.dev.data.IngredientData;
import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.enums.RecipeMediaPath;
import com.happydieting.dev.exception.InvalidRecipeException;
import com.happydieting.dev.model.*;
import com.happydieting.dev.repository.CategoryRepository;
import com.happydieting.dev.repository.IngredientRepository;
import com.happydieting.dev.repository.RecipeRepository;
import com.happydieting.dev.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final NutritionalValueService nutritionalValueService;
    private final MediaService mediaService;
    private final NutritionService nutritionService;
    private final ModelMapper modelMapper;

    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository, IngredientRepository ingredientRepository, UserRepository userRepository, NutritionalValueService nutritionalValueService, MediaService mediaService, NutritionService nutritionService, ModelMapper modelMapper) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
        this.nutritionalValueService = nutritionalValueService;
        this.mediaService = mediaService;
        this.nutritionService = nutritionService;
        this.modelMapper = modelMapper;
    }

    public boolean create(final RecipeData recipeForm) throws InvalidRecipeException {
        RecipeModel newRecipe = new RecipeModel();
        newRecipe.setName(recipeForm.getName());
        newRecipe.setDescription(recipeForm.getDescription());
        newRecipe.setRecipe(recipeForm.getRecipe());
        newRecipe.setTips(recipeForm.getTips());

        UserModel owner = userRepository.findByUsername(recipeForm.getOwner().getUsername()).orElse(null);
        if (owner == null)
            throw new InvalidRecipeException("User is not found. username: " + recipeForm.getOwner().getUsername());

        newRecipe.setOwner(owner);

        newRecipe.setServingAmount(recipeForm.getServingAmount());
        NutritionUnitModel servingUnitModel = nutritionService.getNutritionUnitByCode(recipeForm.getServingUnit().getCode());
        if (servingUnitModel == null)
            throw new InvalidRecipeException("Serving unit is not found. code: " + recipeForm.getServingUnit().getCode());
        newRecipe.setServingUnit(servingUnitModel);

        Set<CategoryModel> categoryModelSet = getCategoryModels(recipeForm.getCategories());
        if (CollectionUtils.isEmpty(categoryModelSet))
            throw new InvalidRecipeException("Categories are not found. codes: "
                    + recipeForm.getCategories().stream().map(CategoryData::getCode).collect(Collectors.joining(",")));

        newRecipe.setCategories(categoryModelSet);

        Set<IngredientModel> ingredientModelSet = getIngredientModels(recipeForm.getIngredients());
        if (CollectionUtils.isEmpty(ingredientModelSet))
            throw new InvalidRecipeException("Ingredients are not found. codes: "
                    + recipeForm.getIngredients().stream().map(IngredientData::getCode).collect(Collectors.joining(",")));
        newRecipe.setIngredients(ingredientModelSet);

        recipeRepository.save(newRecipe);

        mediaService.saveMedia(newRecipe.getId(), RecipeModel.class,
                recipeForm.getImage(),
                RecipeMediaPath.RECIPE_IMAGE_NAME.resolve(newRecipe.getCode()),
                RecipeMediaPath.RECIPE_IMAGE_URL.resolve(newRecipe.getCode()));

        nutritionalValueService.createNutritionalValues(newRecipe, recipeForm.getNutritionalValues());
        return true;
    }

    private Set<CategoryModel> getCategoryModels(List<CategoryData> categories) {
        return new ArrayList<>(categories).stream()
                .map(categoryData -> categoryRepository.findCategoryModelByCode(categoryData.getCode()).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private Set<IngredientModel> getIngredientModels(List<IngredientData> ingredients) {
        return new ArrayList<>(ingredients).stream()
                .map(ingredientData -> ingredientRepository.findIngredientModelByCode(ingredientData.getCode()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public MediaModel getRecipeImage(String recipeCode) {
        Optional<RecipeModel> recipeModelOpt = recipeRepository.findRecipeModelByCode(recipeCode);
        return recipeModelOpt.flatMap(recipeModel -> mediaService.getMediaByOwner(recipeModel.getId(), RecipeModel.class))
                .orElse(null);
    }

    public List<RecipeModel> getRecipeModels(UserModel owner) {
        return recipeRepository.findRecipesByOwner(owner);
    }

    public List<RecipeModel> getRecipeModels(UserModel owner, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<RecipeModel> recipes = recipeRepository.findRecipesByOwner(owner, pageable);
        return recipes.getContent();
    }

    public List<RecipeModel> getRecipeModels(UserModel owner, int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(keyword).ascending());

        Page<RecipeModel> recipes = recipeRepository.findRecipesByOwner(owner, pageable);
        return recipes.getContent();
    }

    public RecipeModel getRecipeByCode(String recipeCode) {
        return recipeRepository.findRecipeModelByCode(recipeCode).orElse(null);
    }

    public List<RecipeModel> getRecipes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<RecipeModel> recipes = recipeRepository.findRecipes(pageable);
        return recipes.getContent();
    }

    public List<RecipeModel> getRecipes(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(keyword).ascending());

        Page<RecipeModel> recipes = recipeRepository.findRecipes(pageable);
        return recipes.getContent();
    }
}

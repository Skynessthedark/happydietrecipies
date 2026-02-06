package com.happydieting.dev.service;

import com.happydieting.dev.data.CategoryData;
import com.happydieting.dev.model.CategoryModel;
import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryData> getCategoryDatas(RecipeModel recipe) {
        return convertModelsToDatas(getCategories(recipe));
    }

    public List<CategoryData> convertModelsToDatas(List<CategoryModel> categories) {
        if(categories == null || categories.isEmpty()) return Collections.emptyList();

        List<CategoryData> categoryDatas = new ArrayList<>();
        for(CategoryModel category: categories){
            categoryDatas.add(convertModelToData(category));
        }

        return categoryDatas;
    }

    public CategoryData convertModelToData(CategoryModel category) {
        CategoryData categoryData = new CategoryData();
        categoryData.setId(category.getId());
        categoryData.setCode(category.getCode());
        categoryData.setName(category.getName());
        categoryData.setDescription(category.getDescription());

        return categoryData;
    }

    public List<CategoryModel> getCategories(RecipeModel recipe) {
        if(recipe == null){
            LOGGER.error("Recipe is null.");
            return Collections.emptyList();
        }

        return categoryRepository.findCategoryModelsByRecipe(recipe);
    }
}

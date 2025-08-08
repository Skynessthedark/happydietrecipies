package com.happydieting.dev.repository;

import com.happydieting.dev.model.NutritionUnitModel;
import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<RecipeModel, Long> {
    Optional<RecipeModel> findRecipeModelById(Long id);
    Optional<RecipeModel> findRecipeModelByCode(String code);
    Optional<RecipeModel> findRecipeModelByOwner(UserModel owner);
}

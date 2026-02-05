package com.happydieting.dev.repository;

import com.happydieting.dev.model.NutritionalValueModel;
import com.happydieting.dev.model.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NutritionalValueRepository extends JpaRepository<NutritionalValueModel, Long> {
    Optional<NutritionalValueModel> findNutritionalValueModelById(Long id);
    List<NutritionalValueModel> findNutritionalValueModelsByRecipe(RecipeModel recipe);
}

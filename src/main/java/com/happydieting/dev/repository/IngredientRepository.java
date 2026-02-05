package com.happydieting.dev.repository;

import com.happydieting.dev.data.IngredientData;
import com.happydieting.dev.model.IngredientModel;
import com.happydieting.dev.model.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<IngredientModel, Long> {
    Optional<IngredientModel> findIngredientModelById(Long id);
    Optional<IngredientModel> findIngredientModelByCode(String code);
    Optional<IngredientModel> findIngredientModelByName(String name);
    boolean existsByCode(String code);

    @Query("SELECT im FROM IngredientModel im " +
            "WHERE LENGTH(:keyword) >= 3 AND LOWER(im.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<IngredientModel> searchByName(@Param("keyword") String keyword);

    @Query("SELECT new com.happydieting.dev.data.IngredientData(im.code, im.name) FROM IngredientModel im " +
            "WHERE LENGTH(:keyword) >= 3 AND LOWER(im.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<IngredientData> searchDataByName(@Param("keyword") String keyword);

    @Query("SELECT im FROM RecipeModel rm JOIN rm.ingredients im WHERE rm = :recipe")
    List<IngredientModel> findIngredientModelsByRecipe(@Param("recipe") RecipeModel recipe);
}

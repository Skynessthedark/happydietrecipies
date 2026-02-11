package com.happydieting.dev.repository;

import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<RecipeModel, Long> {
    Optional<RecipeModel> findRecipeModelById(Long id);
    Optional<RecipeModel> findRecipeModelByCode(String code);

    Page<RecipeModel> findRecipes(Pageable pageable);

    Page<RecipeModel> findRecipesByOwner(UserModel owner, Pageable pageable);

    List<RecipeModel> findRecipesByOwner(UserModel owner);

    @Query("SELECT rm FROM RecipeModel rm JOIN FETCH rm.categories cat WHERE cat.code = :categoryCode")
    List<RecipeModel> findRecipesByCategoryCodeFetch(@Param("categoryCode") String categoryCode);

    @Query("SELECT rm FROM RecipeModel rm JOIN FETCH rm.ingredients ing WHERE ing.code = :ingCode")
    List<RecipeModel> findRecipesByIngredientCodeFetch(@Param("ingCode") String ingredientCode);
}

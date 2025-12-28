package com.happydieting.dev.repository;

import com.happydieting.dev.data.CategoryData;
import com.happydieting.dev.data.IngredientData;
import com.happydieting.dev.model.CategoryModel;
import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
    Optional<CategoryModel> findCategoryModelById(Long id);
    Optional<CategoryModel> findCategoryModelByCode(String code);
    boolean existsByCode(String code);

    @Query("SELECT new com.happydieting.dev.data.CategoryData(cm.code, cm.name) FROM CategoryModel cm " +
            "WHERE LENGTH(:keyword) >= 3 AND LOWER(cm.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CategoryData> searchDataByName(@Param("keyword") String keyword);
}

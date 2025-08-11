package com.happydieting.dev.repository;

import com.happydieting.dev.model.CategoryModel;
import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
    Optional<CategoryModel> findCategoryModelById(Long id);
    Optional<CategoryModel> findCategoryModelByCode(String code);
    boolean existsByCode(String code);
}

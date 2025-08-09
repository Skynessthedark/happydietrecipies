package com.happydieting.dev.repository;

import com.happydieting.dev.model.IngredientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<IngredientModel, Long> {
    Optional<IngredientModel> findIngredientModelById(Long id);
    Optional<IngredientModel> findIngredientModelByName(String name);
    @Query("SELECT im FROM IngredientModel im " +
            "WHERE LENGTH(:keyword) >= 3 AND LOWER(im.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<IngredientModel> searchByName(@Param("keyword") String keyword);
}

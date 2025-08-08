package com.happydieting.dev.repository;

import com.happydieting.dev.model.NutritionTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutritionTypeRepository extends JpaRepository<NutritionTypeModel, Long> {
    Optional<NutritionTypeModel> findNutritionTypeModelById(Long id);
    Optional<NutritionTypeModel> findNutritionTypeModelByCode(String code);
}

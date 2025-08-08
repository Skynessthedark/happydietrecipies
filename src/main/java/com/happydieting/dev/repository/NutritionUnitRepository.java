package com.happydieting.dev.repository;

import com.happydieting.dev.model.NutritionTypeModel;
import com.happydieting.dev.model.NutritionUnitModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutritionUnitRepository extends JpaRepository<NutritionUnitModel, Long> {
    Optional<NutritionUnitModel> findNutritionUnitModelById(Long id);
    Optional<NutritionUnitModel> findNutritionUnitModelByCode(String code);
}

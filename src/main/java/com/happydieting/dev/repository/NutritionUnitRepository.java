package com.happydieting.dev.repository;

import com.happydieting.dev.data.NutritionUnitData;
import com.happydieting.dev.model.NutritionTypeModel;
import com.happydieting.dev.model.NutritionUnitModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NutritionUnitRepository extends JpaRepository<NutritionUnitModel, Long> {
    Optional<NutritionUnitModel> findNutritionUnitModelById(Long id);
    Optional<NutritionUnitModel> findNutritionUnitModelByCode(String code);

    @Query("SELECT new com.happydieting.dev.data.NutritionUnitData(nu.code, nu.name) FROM NutritionUnitModel nu")
    List<NutritionUnitData> findNutritionUnits();
}

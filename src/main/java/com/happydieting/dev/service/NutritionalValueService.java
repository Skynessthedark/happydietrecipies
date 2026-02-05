package com.happydieting.dev.service;

import com.happydieting.dev.data.NutritionalValueData;
import com.happydieting.dev.exception.InvalidNutritionalValueException;
import com.happydieting.dev.model.NutritionTypeModel;
import com.happydieting.dev.model.NutritionalValueModel;
import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.repository.NutritionTypeRepository;
import com.happydieting.dev.repository.NutritionalValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NutritionalValueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NutritionalValueService.class);

    private final NutritionalValueRepository nutritionalValueRepository;
    private final NutritionTypeRepository nutritionTypeRepository;
    private final NutritionService nutritionService;

    public NutritionalValueService(NutritionalValueRepository nutritionalValueRepository, NutritionTypeRepository nutritionTypeRepository, NutritionService nutritionService) {
        this.nutritionalValueRepository = nutritionalValueRepository;
        this.nutritionTypeRepository = nutritionTypeRepository;
        this.nutritionService = nutritionService;
    }

    public void createNutritionalValues(RecipeModel recipe, List<NutritionalValueData> nutritionalValues){
        if(nutritionalValues == null || nutritionalValues.isEmpty() || recipe == null) return;

        for (NutritionalValueData nutritionalValue : nutritionalValues) {
            try{
                validateNutritionalValue(nutritionalValue);

                NutritionalValueModel nutritionalValueModel = new NutritionalValueModel();

                NutritionTypeModel nutritionTypeModel = nutritionTypeRepository
                        .findNutritionTypeModelByCode(nutritionalValue.getNutritionType().getCode())
                        .orElse(null);
                if(nutritionTypeModel == null) continue;

                nutritionalValueModel.setRecipe(recipe);
                nutritionalValueModel.setType(nutritionTypeModel);
                nutritionalValueModel.setValue(nutritionalValue.getValue());

                nutritionalValueRepository.save(nutritionalValueModel);
            }catch (InvalidNutritionalValueException e){
                LOGGER.error("Recipe creation failed.", e);
            }
        }
    }

    private void validateNutritionalValue(NutritionalValueData nutritionalValue) {
        boolean isNotValid = nutritionalValue == null || nutritionalValue.getValue() == null
                || nutritionalValue.getNutritionType() == null
                || nutritionalValue.getNutritionType().getCode() == null;

        if(isNotValid) throw new InvalidNutritionalValueException("Nutritional Value is invalid." );
    }

    public List<NutritionalValueData> getNutritionalValueDatas(RecipeModel recipe) {
        if(recipe == null){
            LOGGER.error("Recipe is null.");
            return Collections.emptyList();
        }

        return convertValuesToDatas(getNutritionalValues(recipe));

    }

    public List<NutritionalValueData> convertValuesToDatas(List<NutritionalValueModel> nutritionalValues) {
        if(nutritionalValues == null || nutritionalValues.isEmpty()) return Collections.emptyList();

        List<NutritionalValueData> nutritionalValueDatas = new ArrayList<>();
        for (NutritionalValueModel nutritionalValue : nutritionalValues) {
            nutritionalValueDatas.add(convertValueToData(nutritionalValue));
        }
        return nutritionalValueDatas;
    }

    public NutritionalValueData convertValueToData(NutritionalValueModel nutritionalValue) {
        if(nutritionalValue == null) return null;
        NutritionalValueData nutritionalValueData = new NutritionalValueData();
        nutritionalValueData.setId(nutritionalValue.getId());
        nutritionalValueData.setValue(nutritionalValue.getValue());
        nutritionalValueData.setRecipeCode(nutritionalValue.getRecipe().getCode());
        nutritionalValueData.setNutritionUnit(nutritionService.getNutritionUnitData(nutritionalValue.getUnit()));
        nutritionalValueData.setNutritionType(nutritionService.getNutritionTypeData(nutritionalValue.getType()));

        return nutritionalValueData;
    }

    public List<NutritionalValueModel> getNutritionalValues(RecipeModel recipe) {
        if(recipe == null){
            LOGGER.error("Recipe is null.");
            return Collections.emptyList();
        }

        return nutritionalValueRepository.findNutritionalValueModelsByRecipe(recipe);
    }
}

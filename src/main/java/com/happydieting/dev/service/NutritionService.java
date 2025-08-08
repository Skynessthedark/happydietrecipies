package com.happydieting.dev.service;

import com.happydieting.dev.data.NutritionalValueData;
import com.happydieting.dev.model.NutritionTypeModel;
import com.happydieting.dev.model.NutritionUnitModel;
import com.happydieting.dev.model.NutritionalValueModel;
import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.repository.NutritionTypeRepository;
import com.happydieting.dev.repository.NutritionUnitRepository;
import com.happydieting.dev.repository.NutritionalValueRepository;
import com.happydieting.dev.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NutritionService {

    private NutritionalValueRepository nutritionalValueRepository;
    private NutritionUnitRepository nutritionUnitRepository;
    private NutritionTypeRepository nutritionTypeRepository;
    private RecipeRepository recipeRepository;

    public NutritionService(NutritionalValueRepository nutritionalValueRepository, NutritionUnitRepository nutritionUnitRepository, NutritionTypeRepository nutritionTypeRepository, RecipeRepository recipeRepository) {
        this.nutritionalValueRepository = nutritionalValueRepository;
        this.nutritionUnitRepository = nutritionUnitRepository;
        this.nutritionTypeRepository = nutritionTypeRepository;
        this.recipeRepository = recipeRepository;
    }

    public boolean saveNutritionUnit(String id, String code, String name){
        if(code == null || name == null){
            //TODO: logging
            return false;
        }

        NutritionUnitModel nutritionUnitModel;

        if(id == null){
            nutritionUnitModel = new NutritionUnitModel();
        }else {
            Optional<NutritionUnitModel> nutritionUnitModelOpt = nutritionUnitRepository.findNutritionUnitModelById(Long.valueOf(id));
            if(nutritionUnitModelOpt.isEmpty()){
                //TODO: logging
                return false;
            }
            nutritionUnitModel = nutritionUnitModelOpt.get();
        }

        nutritionUnitModel.setCode(code);
        nutritionUnitModel.setName(name);
        nutritionUnitRepository.save(nutritionUnitModel);
        return true;
    }

    public boolean saveNutritionType(String id, String code, String name){
        if(code == null || name == null){
            //TODO: logging
            return false;
        }

        NutritionTypeModel nutritionTypeModel;

        if(id == null){
            nutritionTypeModel = new NutritionTypeModel();
        }else {
            Optional<NutritionTypeModel> nutritionTypeModelOpt = nutritionTypeRepository.findNutritionTypeModelById(Long.valueOf(id));
            if(nutritionTypeModelOpt.isEmpty()){
                //TODO: logging
                return false;
            }
            nutritionTypeModel = nutritionTypeModelOpt.get();
        }

        nutritionTypeModel.setCode(code);
        nutritionTypeModel.setName(name);
        nutritionTypeRepository.save(nutritionTypeModel);
        return true;
    }

    public boolean saveNutritionValue(NutritionalValueData nutritionalValueData){
        if(nutritionalValueData == null)
            //TODO: validation required
            return false;

        NutritionalValueModel nutritionalValueModel;

        if(nutritionalValueData.getId() == null){
            nutritionalValueModel = new NutritionalValueModel();
        }else {
            Optional<NutritionalValueModel> nutritionalValueModelOpt =
                    nutritionalValueRepository.findNutritionalValueModelById(Long.valueOf(nutritionalValueData.getId()));
            if(nutritionalValueModelOpt.isEmpty()){
                //TODO: logging
                return false;
            }
            nutritionalValueModel = nutritionalValueModelOpt.get();
        }

        nutritionalValueModel.setValue(nutritionalValueData.getValue());

        Optional<RecipeModel> recipeModelOpt =
                recipeRepository.findRecipeModelById(Long.valueOf(nutritionalValueData.getRecipeId()));
        if(recipeModelOpt.isEmpty()){
            return false;
        }
        nutritionalValueModel.setRecipe(recipeModelOpt.get());

        Optional<NutritionTypeModel> nutritionTypeModelOpt =
                nutritionTypeRepository.findNutritionTypeModelById(Long.valueOf(nutritionalValueData.getNutritionType().getId()));
        if(nutritionTypeModelOpt.isEmpty()){
            return false;
        }
        nutritionalValueModel.setType(nutritionTypeModelOpt.get());

        Optional<NutritionUnitModel> nutritionUnitModelOpt =
                nutritionUnitRepository.findNutritionUnitModelById(Long.valueOf(nutritionalValueData.getNutritionUnit().getId()));
        if(nutritionUnitModelOpt.isEmpty()){
            return false;
        }
        nutritionalValueModel.setUnit(nutritionUnitModelOpt.get());

        nutritionalValueRepository.save(nutritionalValueModel);
        return true;
    }
}

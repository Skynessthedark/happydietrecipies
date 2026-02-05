package com.happydieting.dev.service;

import com.happydieting.dev.data.GenericData;
import com.happydieting.dev.model.NutritionTypeModel;
import com.happydieting.dev.model.NutritionUnitModel;
import com.happydieting.dev.repository.NutritionTypeRepository;
import com.happydieting.dev.repository.NutritionUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NutritionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NutritionService.class);

    private final NutritionUnitRepository nutritionUnitRepository;
    private final NutritionTypeRepository nutritionTypeRepository;

    public NutritionService(NutritionUnitRepository nutritionUnitRepository, NutritionTypeRepository nutritionTypeRepository) {
        this.nutritionUnitRepository = nutritionUnitRepository;
        this.nutritionTypeRepository = nutritionTypeRepository;
    }

    public boolean saveNutritionUnit(String id, String code, String name){
        if(checkCodeAndName(code, name)){
            LOGGER.error("Nutrition Unit code or name is null");
            return false;
        }

        NutritionUnitModel nutritionUnitModel;

        if(id == null){
            nutritionUnitModel = new NutritionUnitModel();
        }else {
            Optional<NutritionUnitModel> nutritionUnitModelOpt = nutritionUnitRepository.findNutritionUnitModelById(Long.valueOf(id));
            if(nutritionUnitModelOpt.isEmpty()){
                LOGGER.error("Nutrition Unit cannot be found by id: {0}", id);
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
        if(checkCodeAndName(code, name)){
            LOGGER.error("Nutrition Type code or name is null");
            return false;
        }

        NutritionTypeModel nutritionTypeModel;

        if(id == null){
            nutritionTypeModel = new NutritionTypeModel();
        }else {
            Optional<NutritionTypeModel> nutritionTypeModelOpt = nutritionTypeRepository.findNutritionTypeModelById(Long.valueOf(id));
            if(nutritionTypeModelOpt.isEmpty()){
                LOGGER.error("Nutrition Type cannot be found by id: {0}", id);
                return false;
            }
            nutritionTypeModel = nutritionTypeModelOpt.get();
        }

        nutritionTypeModel.setCode(code);
        nutritionTypeModel.setName(name);
        nutritionTypeRepository.save(nutritionTypeModel);
        return true;
    }

    public NutritionUnitModel getNutritionUnitByCode(String code){
        if(code == null) return null;

        return nutritionUnitRepository
                .findNutritionUnitModelByCode(code)
                .orElse(null);
    }

    private boolean checkCodeAndName(String code, String name){
        return code == null || code.isEmpty()
                || name == null || name.isEmpty();
    }

    public GenericData getNutritionUnitData(NutritionUnitModel unit) {
        if(unit == null) return null;
        GenericData genericData = new GenericData();
        genericData.setId(unit.getId());
        genericData.setCode(unit.getCode());
        genericData.setName(unit.getName());
        return genericData;
    }

    public GenericData getNutritionTypeData(NutritionTypeModel type) {
        if(type == null) return null;
        GenericData genericData = new GenericData();
        genericData.setId(type.getId());
        genericData.setCode(type.getCode());
        genericData.setName(type.getName());
        return genericData;
    }

    public List<GenericData> getAllNutritionUnits() {
        return nutritionUnitRepository.findAll().stream()
                .map(this::getNutritionUnitData)
                .toList();
    }

    public List<GenericData> getAllNutritionTypes() {
        return nutritionTypeRepository.findAll().stream()
                .map(this::getNutritionTypeData)
                .toList();
    }
}

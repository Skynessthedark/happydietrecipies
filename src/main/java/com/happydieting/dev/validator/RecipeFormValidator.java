package com.happydieting.dev.validator;

import com.happydieting.dev.data.RecipeData;
import org.springframework.stereotype.Component;

@Component
public class RecipeFormValidator {

    public boolean isInvalid(RecipeData recipeForm, Boolean isCreate) {
        if(recipeForm == null)
            return true;

        if(Boolean.TRUE.equals(isCreate))
            return validateForCreate(recipeForm);

        return validateForUpdate(recipeForm);
    }

    private boolean validateForUpdate(RecipeData recipeForm) {
        return isInvalid(recipeForm);
    }

    private boolean validateForCreate(RecipeData recipeForm) {
        return recipeForm.getId() != null || recipeForm.getCode() != null || isInvalid(recipeForm);
    }

    private boolean isInvalid(RecipeData recipeForm){
        return recipeForm.getServingUnit() == null || recipeForm.getServingUnit().getCode() == null
                || recipeForm.getIngredients() == null || recipeForm.getIngredients().isEmpty()
                || recipeForm.getCategories() == null || recipeForm.getCategories().isEmpty();
    }
}

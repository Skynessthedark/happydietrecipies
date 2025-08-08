package com.happydieting.dev.listener;

import com.happydieting.dev.model.RecipeModel;
import com.happydieting.dev.util.RecipeUtil;
import jakarta.persistence.PrePersist;

public class RecipeAuditListener {

    @PrePersist
    public void prePersist(RecipeModel entity) {
        entity.setCode(RecipeUtil.generateRecipeCode(entity.getName()));
    }


}

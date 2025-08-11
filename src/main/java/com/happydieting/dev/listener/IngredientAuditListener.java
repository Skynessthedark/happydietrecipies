package com.happydieting.dev.listener;

import com.happydieting.dev.config.SpringContext;
import com.happydieting.dev.model.IngredientModel;
import com.happydieting.dev.repository.IngredientRepository;
import jakarta.persistence.PrePersist;
import org.apache.commons.lang3.RandomStringUtils;

public class IngredientAuditListener {

    @PrePersist
    public void prePersist(IngredientModel entity) {
        if(entity.getCode() == null){
            String newCode;
            do {
                newCode = RandomStringUtils.secure().nextAlphanumeric(8).toUpperCase();
            } while (getIngredientRepository().existsByCode(newCode));
            entity.setCode(newCode);
        }
    }

    private IngredientRepository getIngredientRepository() {
        return SpringContext.getBean(IngredientRepository.class);
    }

}

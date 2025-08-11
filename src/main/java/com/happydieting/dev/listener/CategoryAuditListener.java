package com.happydieting.dev.listener;

import com.happydieting.dev.config.SpringContext;
import com.happydieting.dev.model.CategoryModel;
import com.happydieting.dev.repository.CategoryRepository;
import jakarta.persistence.PrePersist;
import org.apache.commons.lang3.RandomStringUtils;

public class CategoryAuditListener {

    @PrePersist
    public void prePersist(CategoryModel entity) {
        if(entity.getCode() == null){
            String newCode;
            do {
                newCode = RandomStringUtils.secure().nextAlphanumeric(6).toUpperCase();
            } while (getCategoryRepository().existsByCode(newCode));
            entity.setCode(newCode);
        }
    }

    private CategoryRepository getCategoryRepository() {
        return SpringContext.getBean(CategoryRepository.class);
    }


}

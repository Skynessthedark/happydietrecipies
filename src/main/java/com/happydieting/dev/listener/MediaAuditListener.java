package com.happydieting.dev.listener;

import com.happydieting.dev.config.SpringContext;
import com.happydieting.dev.model.MediaModel;
import com.happydieting.dev.repository.MediaRepository;
import jakarta.persistence.PrePersist;
import org.apache.commons.lang3.RandomStringUtils;

public class MediaAuditListener {

    @PrePersist
    public void prePersist(MediaModel entity) {
        if(entity.getCode() == null){
            String newCode;
            do {
                newCode = RandomStringUtils.secure().nextAlphanumeric(10).toUpperCase();
            } while (getMediaRepository().existsByCode(newCode));
            entity.setCode(newCode);
        }
    }

    private MediaRepository getMediaRepository() {
        return SpringContext.getBean(MediaRepository.class);
    }
}

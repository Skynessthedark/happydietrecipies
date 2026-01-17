package com.happydieting.dev.service;

import com.happydieting.dev.model.MediaModel;
import com.happydieting.dev.model.MediaOwner;
import com.happydieting.dev.repository.MediaRepository;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class MediaService{

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaService.class);

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Optional<MediaModel> getMediaByOwner(Long ownerId, Class<?> ownerType){
        if(ownerId != null && ownerType != null)
            return mediaRepository.findByOwner_IdAndType(ownerId, ownerType.getName());

        return Optional.empty();
    }

    public String getMediaUrlByOwner(Long ownerId, Class<?> ownerType){
        Optional<MediaModel> mediaModelOpt = getMediaByOwner(ownerId, ownerType);
        if(mediaModelOpt.isPresent() && StringUtils.hasText(mediaModelOpt.get().getMediaUrl())){
            return mediaModelOpt.get().getMediaUrl();
        }
        return Strings.EMPTY;
    }

    public void saveMedia(Long ownerId, Class<?> type, MultipartFile fileData, String fileName, String mediaUrl) {
        if(Objects.nonNull(fileData)){
            try {
                Optional<MediaModel> mediaModelOpt = getMediaByOwner(ownerId, type);
                MediaModel mediaModel = mediaModelOpt.orElse(null);
                if(mediaModelOpt.isEmpty()){
                    mediaModel = new MediaModel();
                    mediaModel.setOwner(new MediaOwner(ownerId, type.getName()));
                    mediaModel.setFileName(fileName);
                    mediaModel.setMediaUrl(mediaUrl);
                }
                mediaModel.setContent(fileData.getBytes());
                mediaModel.setContentType(fileData.getContentType());
                mediaRepository.save(mediaModel);
            } catch (IOException e) {
                LOGGER.error("saveMediaExp:", e);
            }
        }
    }
}

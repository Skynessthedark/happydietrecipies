package com.happydieting.dev.facade;

import com.happydieting.dev.data.UserData;
import com.happydieting.dev.model.MediaModel;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.service.MediaService;
import com.happydieting.dev.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Logger;

@Component
public class UserFacade {

    private final Logger logger = Logger.getLogger(UserFacade.class.getName());
    private final UserService userService;
    private final MediaService mediaService;

    public UserFacade(UserService userService, MediaService mediaService) {
        this.userService = userService;
        this.mediaService = mediaService;
    }

    public UserData getUserForProfile(UserModel user) {
        if (user == null) {
            // TODO: English
            logger.warning("User metodu null user ile çağrıldı.");
            return null;
        }

        logger.info("User verisi dönüştürülüyor");
        UserData userData = userService.convertModel2Data(user);

        //TODO: Media servisten getir (Tamamlandı)
        String mediaUrl = mediaService.getMediaUrlByOwner(user.getId(), UserModel.class);
        userData.setImageUrl(mediaUrl);

        return userData;
    }


    public MediaModel getUserImage(String username) {
        return userService.getUserImageUrl(username);
    }

    // TODO: Update profile
    public boolean updateUser(UserData userData, MultipartFile image) {
        return userService.updateUser(userData, image);
    }
}


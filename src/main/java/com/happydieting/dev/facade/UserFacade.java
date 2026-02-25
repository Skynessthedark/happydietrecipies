package com.happydieting.dev.facade;


import com.happydieting.dev.data.UserData;
import com.happydieting.dev.model.MediaModel;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.util.logging.Logger;


@Component
public class UserFacade {


    private final Logger logger = Logger.getLogger(UserFacade.class.getName());


    private final UserService userService;


    public UserFacade(UserService userService) {
        this.userService = userService;
    }


    public UserData getUserForProfile(UserModel user) {
        if (user == null) {
            logger.warning("User metodu null user ile çağrıldı.");
            return null;
        }


        logger.info("User verisi dönüştürülüyor");
        UserData userData = userService.convertModel2Data(user);
        userData.setImageUrl("/user/" + userData.getUsername() + "/image");
        return userData;
    }


    public MediaModel getUserImage(String username) {
        return userService.getUserImageUrl(username);
    }


    // TODO: Update profile
    public boolean updateUser(String username, UserData userData, MultipartFile image) {
        return userService.updateUser(username, userData, image);
    }
}


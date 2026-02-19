package com.happydieting.dev.facade;

import com.happydieting.dev.data.UserData;
import com.happydieting.dev.model.MediaModel;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.UserRepository;
import com.happydieting.dev.service.MediaService;
import com.happydieting.dev.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Logger;

@Component
public class UserFacade {

    private final Logger logger = Logger.getLogger(UserFacade.class.getName());

    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }


    public UserData getUserForProfile(UserModel user) {
        UserData userData = userService.convertModel2Data(user);
        userData.setImageUrl("/user/" + userData.getUsername() + "/image");
        return userData;
    }

    public MediaModel getUserImage(String username) {
        return userService.getUserImageUrl(username);
    }
}

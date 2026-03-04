package com.happydieting.dev.controller;

import com.happydieting.dev.constant.ControllerConstant;
import com.happydieting.dev.data.UserData;
import com.happydieting.dev.facade.UserFacade;
import com.happydieting.dev.model.MediaModel;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.UserRepository;
import com.happydieting.dev.util.MediaUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping(ControllerConstant.USER)
public class UserController {
    private final UserFacade userFacade;
    private final UserRepository userRepository;

    public UserController(UserFacade userFacade, UserRepository userRepository) {
        this.userFacade = userFacade;
        this.userRepository = userRepository;
    }

    @GetMapping("/{username}/image")
    public void getUserImage(@PathVariable String username,
                             HttpServletResponse response) throws IOException {
        MediaModel media = userFacade.getUserImage(username);
        MediaUtil.printMedia(media, response);
    }

    //TODO: username logic olarak gereksiz, sadece /update-profile olarak path belirlenebilir.
    @GetMapping(ControllerConstant.UPDATE_PROFILE)
    public String getUpdateProfile(java.security.Principal principal, Model model) {
        String username = principal.getName();

        UserModel user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ControllerConstant.REDIRECT + ControllerConstant.MyAccount.PROFILE;
        }

        UserData userData = userFacade.getUserForProfile(user);
        model.addAttribute("profileForm", userData);

        return "profile/update-profile";
    }

    @PostMapping(ControllerConstant.UPDATE_PROFILE)
    public String updateUser(@ModelAttribute("profileForm") UserData userData,
                             @RequestParam(value = "imageFile", required = false) MultipartFile image) {
        boolean isSaved = userFacade.updateUser(userData, image);
        if (!isSaved) {
            return ControllerConstant.REDIRECT_ERROR;
        }
        return ControllerConstant.REDIRECT + ControllerConstant.USER + ControllerConstant.UPDATE_PROFILE;
    }
}


package com.happydieting.dev.service;

import com.happydieting.dev.data.RegisterData;
import com.happydieting.dev.data.UserData;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.UserRepository;
import com.happydieting.dev.enums.RecipeMediaPath;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final MediaService mediaService;
    private final PasswordEncoder passwordEncoder;

    public UserService(ModelMapper modelMapper,
                       UserRepository userRepository,
                       MediaService mediaService,
                       @Lazy PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.mediaService = mediaService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean createUser(RegisterData registerForm, MultipartFile image) {
        if (registerForm == null || registerForm.getUserData() == null) {
            return false;
        }

        UserData userData = registerForm.getUserData();

        if (userRepository.findByUsername(userData.getUsername()).isPresent()) {
            return false;
        }

        UserModel newUser = new UserModel();
        newUser.setUsername(userData.getUsername());
        newUser.setFullName(userData.getFullName());
        newUser.setEmail(userData.getEmail());
        newUser.setBio(userData.getBio());

        if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userData.getPassword());
            newUser.setPassword(encodedPassword);
        } else {
            return false; // Şifresiz kullanıcı kaydedilemez
        }

        userRepository.save(newUser);

        // Profil resmi işlemleri
        if (image != null && !image.isEmpty()) {
            mediaService.saveMedia(newUser.getId(), UserModel.class, image,
                    RecipeMediaPath.RECIPE_IMAGE_NAME.resolve(newUser.getUsername()),
                    RecipeMediaPath.RECIPE_IMAGE_URL.resolve(newUser.getUsername()));
        }

        return true;
    }

    public UserData convertModel2Data(UserModel user) {
        if (Objects.isNull(user)) return null;
        UserData userData = modelMapper.map(user, UserData.class);
        userData.setPassword(null);
        return userData;
    }
}
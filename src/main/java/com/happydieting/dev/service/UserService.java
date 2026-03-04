package com.happydieting.dev.service;


import com.happydieting.dev.data.RegisterData;
import com.happydieting.dev.data.UserData;
import com.happydieting.dev.enums.UserMediaPath;
import com.happydieting.dev.model.MediaModel;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;


@Service
public class UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final MediaService mediaService;
    private final PasswordEncoder passwordEncoder;


    public UserService(ModelMapper modelMapper, UserRepository userRepository, MediaService mediaService, @Lazy PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.mediaService = mediaService;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public boolean createUser(RegisterData registerForm, MultipartFile image) {
        if (registerForm == null || registerForm.getEmail() == null) {
            return false;
        }
        if (userRepository.findByUsername(registerForm.getEmail()).isPresent()) {
            return false;
        }
        UserModel newUser = new UserModel();
        newUser.setUsername(registerForm.getEmail());
        newUser.setFullName(registerForm.getFullName());
        newUser.setEmail(registerForm.getEmail());
        newUser.setBio(registerForm.getBio());
        if (registerForm.getPassword() != null && !registerForm.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(registerForm.getPassword());
            newUser.setPassword(encodedPassword);
        } else {
            return false;
        }
        userRepository.save(newUser);
        mediaService.saveMedia(newUser.getId(), UserModel.class, image, UserMediaPath.IMAGE_NAME.resolve(newUser.getUsername()), UserMediaPath.IMAGE_URL.resolve(newUser.getUsername()));
        return true;
    }


    @Transactional
    public boolean updateUser(UserData userData, MultipartFile image) {
        if (userData == null || userData.getEmail() == null) {
            return false;
        }

        Optional<UserModel> optionalUser = userRepository.findByUsername(userData.getEmail());
        if (optionalUser.isEmpty()) {
            return false;
        }
        UserModel user = optionalUser.get();

        //TODO: basit alanlar için mapper kullanılabilir
        // Alan güncellemeleri
        if (userData.getFullName() != null) user.setFullName(userData.getFullName());
        if (userData.getBio() != null) user.setBio(userData.getBio());
        if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userData.getPassword()));
        }

        // TODO: image kısmı
        if (image != null && !image.isEmpty()) {
            // mediaService.saveMedia metodunun mevcut resmi override ettiğini varsayıyoruz.
            mediaService.saveMedia(
                    user.getId(),
                    UserModel.class,
                    image,
                    UserMediaPath.IMAGE_NAME.resolve(user.getUsername()),
                    UserMediaPath.IMAGE_URL.resolve(user.getUsername())
            );
        }

        userRepository.save(user);
        return true;
    }

    // TODO: Şu kısım sanki gereksiz gibi buraya tekrar bak
    public MediaModel getUserImageUrl(String username) {
        UserModel user = userRepository.findByUsername(username).get();
        return mediaService.getMediaByOwner(user.getId(), UserModel.class).orElse(null);
    }

    public UserData convertModel2Data(UserModel user) {
        if (Objects.isNull(user)) return null;
        UserData userData = modelMapper.map(user, UserData.class);
        userData.setPassword(null);
        return userData;
    }
}
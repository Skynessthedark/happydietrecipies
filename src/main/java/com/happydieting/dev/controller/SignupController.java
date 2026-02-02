package com.happydieting.dev.controller;

import com.happydieting.dev.data.RegisterData;
import com.happydieting.dev.data.UserData;
import com.happydieting.dev.model.MediaModel;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.UserRepository;
import com.happydieting.dev.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;
    private final UserRepository userRepository;

    public SignupController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getSignupPage(Model model) {
        model.addAttribute("registerForm", new RegisterData());
        return "signup";
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String register(
            @Valid @ModelAttribute("registerForm") RegisterData registerForm,
            BindingResult bindingResult,
            @RequestParam("rePassword") String rePassword,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Model model) {


        if (registerForm.getUserData() != null &&
                !registerForm.getUserData().getPassword().equals(rePassword)) {
            bindingResult.rejectValue("userData.password", "error.password", "Şifreler uyuşmuyor.");
        }

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        boolean isCreated = userService.createUser(registerForm, image);
        if (isCreated) {
            return "redirect:/login";
        } else {
            model.addAttribute("errorMessage", "Kayıt sırasında bir hata oluştu veya kullanıcı mevcut.");
            return "signup";
        }


    }

   /*
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String register(
            @Valid @ModelAttribute("registerForm") UserModel user,
            BindingResult bindingResult,
            @RequestParam("rePassword") String rePassword,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Model model) {

        // Şifre kontrolü
        if (!user.getPassword().equals(rePassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }

        // Public username kontrol
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "signup";
        }

        // Spring Security için username = email
        user.setUsername(user.getEmail());

        // Password encode
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Profil foto (opsiyonel)
        if (image != null && !image.isEmpty()) {
            try {
                MediaModel media = new MediaModel();

                // Tekrar etme olasılığı sunan bir kimlik oluşturur.
                media.setCode(UUID.randomUUID().toString());
                media.setFileName(image.getOriginalFilename());
                media.setContentType(image.getContentType());
                media.setContent(image.getBytes());

                user.setImage(media);

            } catch (IOException e) {
                model.addAttribute("error", "Image upload failed");
                return "signup";
            }
        }


        userRepository.save(user);
        return "redirect:/login";
    }
   */
}

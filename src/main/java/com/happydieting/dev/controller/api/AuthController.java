package com.happydieting.dev.controller.api;

import com.happydieting.dev.data.AuthData;
import com.happydieting.dev.data.RegisterData;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.UserRepository;
import com.happydieting.dev.security.service.CustomUserDetailsService;
import com.happydieting.dev.security.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService customUserDetailsService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ===================== LOGIN =====================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthData authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hatalı giriş");
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Collections.singletonMap("token", jwt));
    }

    // ===================== REGISTER =====================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterData data) {

        if (userRepository.existsByUsername(data.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username already exists");
        }

        if (userRepository.existsByEmail(data.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email already exists");
        }

        UserModel user = new UserModel();
        user.setUsername(data.getUsername());
        user.setEmail(data.getEmail());
        user.setFullName(data.getFullName());
        user.setPassword(passwordEncoder.encode(data.getPassword()));

        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }
}

package com.happydieting.dev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happydieting.dev.constant.ControllerConstant;
import com.happydieting.dev.controller.api.AuthController;
import com.happydieting.dev.data.AuthData;
import com.happydieting.dev.security.filter.JwtTokenFilter;
import com.happydieting.dev.security.handler.CustomAuthenticationSuccessHandler;
import com.happydieting.dev.security.service.CustomUserDetailsService;
import com.happydieting.dev.security.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtTokenFilter jwtTokenFilter;

    @MockitoBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get token for api login")
    void getTokenForLoginApi_shouldGiveToken() throws Exception {

        String username = "test-user";
        String password = "1234";
        String jwtToken = "mock-jwt-token";
        String contentType = "application/json";

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);

        UserDetails userDetails = new User(username, password, new ArrayList<>());

        Mockito.when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        Mockito.when(jwtUtil.generateToken(username)).thenReturn("mock-jwt-token");

        AuthData authData = new AuthData();
        authData.setUsername(username);
        authData.setPassword(password);

        mockMvc.perform(post(ControllerConstant.API + ControllerConstant.AUTH + ControllerConstant.LOGIN)
                        .contentType(contentType)
                        .accept(contentType)
                        .content(objectMapper.writeValueAsString(authData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwtToken));
    }

}


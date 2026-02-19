package com.happydieting.dev.controller;

import com.happydieting.dev.constant.ControllerConstant;
import com.happydieting.dev.security.service.CustomUserDetailsService;
import com.happydieting.dev.security.util.JwtUtil;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomErrorController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomErrorControllerTest {

    private static final String ERROR_VIEW = "error/error";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private static RequestPostProcessor dispatcherType(DispatcherType type) {
        return request -> {
            request.setDispatcherType(type);
            return request;
        };
    }

    @Test
    @DisplayName("When DispatcherType is not ERROR return default error view")
    void error_whenNotErrorDispatchType_shouldReturnDefaultErrorPage() throws Exception {
        mockMvc.perform(get(ControllerConstant.ERROR).with(dispatcherType(DispatcherType.REQUEST)))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("When 404, should return error/404 view")
    void error_when404_shouldReturn404PageAndModel() throws Exception {
        mockMvc.perform(get(ControllerConstant.ERROR)
                        .with(dispatcherType(DispatcherType.ERROR))
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/recipes/does-not-exist"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("status", is(404)))
                .andExpect(model().attribute("path", is("/recipes/does-not-exist")));
    }

    @Test
    @DisplayName("When 403, should return error/403 view")
    void error_when403_shouldReturn403Page() throws Exception {
        mockMvc.perform(get(ControllerConstant.ERROR)
                        .with(dispatcherType(DispatcherType.ERROR))
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 403)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/me/recipes"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/403"))
                .andExpect(model().attribute("status", is(403)))
                .andExpect(model().attribute("path", is("/me/recipes")));
    }

    @Test
    @DisplayName("When 500, should return error/500 view and exception message should be added to model")
    void error_when500WithException_shouldReturn500PageAndMessage() throws Exception {
        RuntimeException ex = new RuntimeException("boom");

        mockMvc.perform(get(ControllerConstant.ERROR)
                        .with(dispatcherType(DispatcherType.ERROR))
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/recipes/new")
                        .requestAttr(RequestDispatcher.ERROR_EXCEPTION, ex))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500"))
                .andExpect(model().attribute("status", is(500)))
                .andExpect(model().attribute("path", is("/recipes/new")))
                .andExpect(model().attribute("message", is("boom")));
    }

    @Test
    @DisplayName("When unknown status occurres, should return error/error view")
    void error_whenUnknownStatus_shouldReturnGenericErrorPage() throws Exception {
        mockMvc.perform(get(ControllerConstant.ERROR)
                        .with(dispatcherType(DispatcherType.ERROR))
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 418)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/teapot"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW))
                .andExpect(model().attribute("status", is(418)))
                .andExpect(model().attribute("path", is("/teapot")));
    }
}


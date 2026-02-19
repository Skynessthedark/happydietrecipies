package com.happydieting.dev.controller;

import com.happydieting.dev.constant.ControllerConstant;
import com.happydieting.dev.data.IngredientData;
import com.happydieting.dev.repository.IngredientRepository;
import com.happydieting.dev.security.handler.CustomAuthenticationSuccessHandler;
import com.happydieting.dev.security.service.CustomUserDetailsService;
import com.happydieting.dev.security.service.SessionService;
import com.happydieting.dev.security.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(IngredientController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private IngredientRepository ingredientRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Test
    @DisplayName("Search with given parameter and return ingredient data")
    void getIngredients_withDefaultParam() throws Exception {
        String ingredientName = "test-ingredient";

        Mockito.when(sessionService.getSessionUser()).thenReturn(null);
        Mockito.when(ingredientRepository.searchDataByName(ingredientName))
                .thenReturn(getSampleIngredients(ingredientName));

        mockMvc.perform(get(ControllerConstant.INGREDIENT + ControllerConstant.SEARCH)
                        .param("ingredientName", ingredientName))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    private List<IngredientData> getSampleIngredients(String ingredientName){
        IngredientData ingredientData = new IngredientData();
        ingredientData.setId(1L);
        ingredientData.setCode("SAMPLE");
        ingredientData.setName(ingredientName);

        List<IngredientData> sampleIngredients = new ArrayList<>();
        sampleIngredients.add(ingredientData);
        return sampleIngredients;
    }
}


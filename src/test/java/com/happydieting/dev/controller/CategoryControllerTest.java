package com.happydieting.dev.controller;

import com.happydieting.dev.constant.ControllerConstant;
import com.happydieting.dev.data.CategoryData;
import com.happydieting.dev.repository.CategoryRepository;
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

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private CategoryRepository categoryRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Test
    @DisplayName("Search with given parameter and return category data")
    void getCategories_withDefaultParam() throws Exception {
        String categoryName = "test-category";

        Mockito.when(sessionService.getSessionUser()).thenReturn(null);
        Mockito.when(categoryRepository.searchDataByName(categoryName))
                .thenReturn(getSampleCategories(categoryName));

        mockMvc.perform(get(ControllerConstant.CATEGORY + ControllerConstant.SEARCH)
                        .param("categoryName", categoryName))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    private List<CategoryData> getSampleCategories(String categoryName) {
        CategoryData categoryData = new CategoryData();
        categoryData.setId(1L);
        categoryData.setCode("SAMPLE");
        categoryData.setName(categoryName);

        List<CategoryData> sampleCategories = new ArrayList<>();
        sampleCategories.add(categoryData);
        return sampleCategories;
    }
}


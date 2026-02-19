package com.happydieting.dev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happydieting.dev.constant.ControllerConstant;
import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.data.UserData;
import com.happydieting.dev.facade.RecipeFacade;
import com.happydieting.dev.security.filter.JwtTokenFilter;
import com.happydieting.dev.security.handler.CustomAuthenticationSuccessHandler;
import com.happydieting.dev.security.service.CustomUserDetailsService;
import com.happydieting.dev.security.service.SessionService;
import com.happydieting.dev.service.NutritionService;
import com.happydieting.dev.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NutritionService nutritionService;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RecipeFacade recipeFacade;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtTokenFilter jwtTokenFilter;

    @MockitoBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Test
    @DisplayName("GET /recipes with default params should return list page")
    void getRecipeListPage_defaultParams_shouldReturnView() throws Exception {
        Mockito.when(recipeFacade.getRecipes(0, 10)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(ControllerConstant.RECIPES))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/list"))
                .andExpect(model().attributeExists("recipes"))
                .andExpect(model().attribute("recipes", hasSize(0)));

        Mockito.verify(recipeFacade).getRecipes(0, 10);
    }

    @Test
    @DisplayName("GET /recipes with default params and sort param should return sorted list")
    void getRecipeListPage_withSort_shouldCallSortedFacade() throws Exception {
        Mockito.when(recipeFacade.getRecipes(0, 10, "name")).thenReturn(Collections.emptyList());

        mockMvc.perform(get(ControllerConstant.RECIPES).param("sort", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/list"))
                .andExpect(model().attributeExists("recipes"));

        Mockito.verify(recipeFacade).getRecipes(0, 10, "name");
    }

    @Test
    @DisplayName("GET /recipes/new should return new recipe page with model attributes")
    void getRecipeAddPage_shouldReturnNewRecipeViewWithModel() throws Exception {
        Mockito.when(nutritionService.getAllNutritionUnits()).thenReturn(List.of());
        Mockito.when(nutritionService.getAllNutritionTypes()).thenReturn(List.of());

        mockMvc.perform(get(ControllerConstant.RECIPES + "/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/new-recipe"))
                .andExpect(model().attributeExists("recipeForm"))
                .andExpect(model().attributeExists("servingUnits"))
                .andExpect(model().attributeExists("nutritionalTypes"));

        Mockito.verify(nutritionService).getAllNutritionUnits();
        Mockito.verify(nutritionService).getAllNutritionTypes();
    }

    @Test
    @DisplayName("POST /recipes/new without session user should be return fail response")
    void addNewRecipe_whenNoSessionUser_shouldReturnFailureResponse() throws Exception {
        RecipeData recipeForm = new RecipeData();
        recipeForm.setName("Test");
        recipeForm.setDescription("Desc");
        recipeForm.setRecipe("Steps");
        recipeForm.setServingAmount(1);

        MockMultipartFile recipePart = new MockMultipartFile(
                "recipeForm",
                "recipeForm.json",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(recipeForm)
        );

        Mockito.when(sessionService.getSessionUser()).thenReturn(null);
        Mockito.when(userService.convertModel2Data(null)).thenReturn(null);

        mockMvc.perform(multipart(ControllerConstant.RECIPES + "/new")
                        .file(recipePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(false)))
                .andExpect(jsonPath("$.message", is("No session user.")));

        Mockito.verify(recipeFacade, Mockito.never()).create(any());
    }

    @Test
    @DisplayName("POST /recipes/new should return success response")
    void addNewRecipe_whenValid_shouldReturnSuccessResponse() throws Exception {
        RecipeData recipeForm = new RecipeData();
        recipeForm.setName("Test");
        recipeForm.setDescription("Desc");
        recipeForm.setRecipe("Steps");
        recipeForm.setServingAmount(2);

        MockMultipartFile recipePart = new MockMultipartFile(
                "recipeForm",
                "recipeForm.json",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(recipeForm)
        );

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake".getBytes(StandardCharsets.UTF_8)
        );

        UserData owner = new UserData();
        owner.setUsername("u1");
        Mockito.when(sessionService.getSessionUser()).thenReturn(null);
        Mockito.when(userService.convertModel2Data(null)).thenReturn(owner);
        Mockito.when(recipeFacade.create(any(RecipeData.class))).thenReturn(true);

        mockMvc.perform(multipart(ControllerConstant.RECIPES + "/new")
                        .file(recipePart)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(true)))
                .andExpect(jsonPath("$.message", is("Recipe created successfully.")));

        ArgumentCaptor<RecipeData> captor = ArgumentCaptor.forClass(RecipeData.class);
        Mockito.verify(recipeFacade).create(captor.capture());
        RecipeData captured = captor.getValue();
        assertNotNull(captured.getOwner());
        assertEquals("u1", captured.getOwner().getUsername());
    }
}


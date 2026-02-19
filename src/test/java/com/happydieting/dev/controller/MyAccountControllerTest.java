package com.happydieting.dev.controller;

import com.happydieting.dev.constant.ControllerConstant;
import com.happydieting.dev.data.RecipeData;
import com.happydieting.dev.facade.RecipeFacade;
import com.happydieting.dev.security.filter.JwtTokenFilter;
import com.happydieting.dev.security.handler.CustomAuthenticationSuccessHandler;
import com.happydieting.dev.security.service.CustomUserDetailsService;
import com.happydieting.dev.security.service.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MyAccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class MyAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private RecipeFacade recipeFacade;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtTokenFilter jwtTokenFilter;

    @MockitoBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Test
    @DisplayName("Success with Default Parameters")
    void getMyRecipesPage_defaultParams_shouldReturnViewAndModel() throws Exception {
        Mockito.when(sessionService.getSessionUser()).thenReturn(null);
        Mockito.when(recipeFacade.getRecipesOfUser(any(), eq(0), eq(9)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get(ControllerConstant.MyAccount.MY_ACCOUNT + ControllerConstant.RECIPES))
                .andExpect(status().isOk())
                .andExpect(view().name("my-account/recipes"))
                .andExpect(model().attributeExists("recipes"))
                .andExpect(model().attribute("recipes", hasSize(0)))
                .andExpect(model().attribute("page", is(0)))
                .andExpect(model().attribute("size", is(9)))
                .andExpect(model().attribute("hasPrev", is(false)))
                .andExpect(model().attribute("hasNext", is(false)));
    }

    @Test
    @DisplayName("Sorting Parameter Test")
    void getMyRecipesPage_withSort_shouldUseSortedMethod() throws Exception {
        RecipeData recipe = new RecipeData();
        recipe.setName("Test Recipe");
        List<RecipeData> recipeList = List.of(recipe);

        Mockito.when(sessionService.getSessionUser()).thenReturn(null);
        Mockito.when(recipeFacade.getRecipesOfUser(any(), eq(0), eq(9), eq("createdDate")))
                .thenReturn(recipeList);

        mockMvc.perform(get(ControllerConstant.MyAccount.MY_ACCOUNT + ControllerConstant.RECIPES)
                        .param("sort", "createdDate"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-account/recipes"))
                .andExpect(model().attributeExists("recipes"))
                .andExpect(model().attribute("recipes", hasSize(1)))
                .andExpect(model().attribute("page", is(0)))
                .andExpect(model().attribute("size", is(9)))
                .andExpect(model().attribute("hasPrev", is(false)))
                .andExpect(model().attribute("hasNext", is(false)));

        Mockito.verify(recipeFacade).getRecipesOfUser(any(), eq(0), eq(9), eq("createdDate"));
    }

    @Test
    @DisplayName("When page is full, has next should be true")
    void getMyRecipesPage_hasNextFlag_shouldBeTrueWhenPageFull() throws Exception {
        RecipeData r1 = new RecipeData();
        RecipeData r2 = new RecipeData();
        List<RecipeData> recipeList = List.of(r1, r2);

        Mockito.when(sessionService.getSessionUser()).thenReturn(null);
        Mockito.when(recipeFacade.getRecipesOfUser(any(), eq(1), eq(2)))
                .thenReturn(recipeList);

        mockMvc.perform(get(ControllerConstant.MyAccount.MY_ACCOUNT + ControllerConstant.RECIPES)
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-account/recipes"))
                .andExpect(model().attribute("page", is(1)))
                .andExpect(model().attribute("size", is(2)))
                .andExpect(model().attribute("hasPrev", is(true)))
                .andExpect(model().attribute("hasNext", is(true)));
    }
}


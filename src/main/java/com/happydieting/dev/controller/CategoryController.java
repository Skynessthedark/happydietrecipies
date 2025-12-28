package com.happydieting.dev.controller;

import com.happydieting.dev.data.CategoryData;
import com.happydieting.dev.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/search")
    @ResponseBody
    public List<CategoryData> searchCategories(@RequestParam String categoryName) {
        return categoryRepository.searchDataByName(categoryName);
    }
}

package com.happydieting.dev.model;

import com.happydieting.dev.listener.CategoryAuditListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@EntityListeners({CategoryAuditListener.class})
@Table(name = "CATEGORY")
public class CategoryModel extends ItemModel{

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<RecipeModel> recipes;

    @ManyToMany
    @JoinTable(name = "CATEGORY2SUBCATEGORY_REL",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
    private Set<CategoryModel> subcategories = new HashSet<>();

    @ManyToMany(mappedBy = "subcategories", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Set<CategoryModel> parentCategories = new HashSet<>();

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<IngredientModel> ingredients = new HashSet<>();

}

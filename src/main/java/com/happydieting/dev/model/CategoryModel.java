package com.happydieting.dev.model;

import com.happydieting.dev.listener.CategoryAuditListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
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

    @ManyToMany(mappedBy = "subcategories", cascade = CascadeType.DETACH)
    private Set<CategoryModel> parentCategories = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    private Set<IngredientModel> ingredients = new HashSet<>();

}

package com.happydieting.dev.model;

import com.happydieting.dev.listener.RecipeAuditListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "RECIPE")
@EntityListeners(RecipeAuditListener.class)
public class RecipeModel extends ItemModel{

    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private String recipe;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MediaModel image;

    private String tips;
    private Double servingAmount;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserModel owner;

    @ManyToOne
    @JoinColumn
    private NutritionUnitModel servingUnit;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<NutritionalValueModel> nutritionalValues;

    @ManyToMany
    @JoinTable(name = "RECIPE2INGREDIENT_REL",
            joinColumns = {@JoinColumn(name = "recipe_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "ingredient_id", referencedColumnName = "id")})
    private Set<IngredientModel> ingredients;

    @ManyToMany
    @JoinTable(
            name = "RECIPE2CATEGORY_REL",
            joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<CategoryModel> categories;
}

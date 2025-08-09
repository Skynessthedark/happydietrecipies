package com.happydieting.dev.model;

import com.happydieting.dev.listener.RecipeAuditListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
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
    //@Column(nullable = false)
    private byte[] image;

    private String tips;
    private Double nutritionValue;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserModel owner;

    @ManyToOne
    @JoinColumn
    private NutritionUnitModel nutritionUnit;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NutritionalValueModel> nutritionalValues;

    @ManyToMany
    @JoinTable(name = "RECIPE2INGREDIENT_REL",
            joinColumns = {@JoinColumn(name = "recipe_id")},
            inverseJoinColumns = {@JoinColumn(name = "ingredient_id")})
    private List<IngredientModel> ingredients;

    //private List categories;
}

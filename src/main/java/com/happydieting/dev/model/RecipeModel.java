package com.happydieting.dev.model;

import com.happydieting.dev.listener.RecipeAuditListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "RECIPE")
@EntityListeners(RecipeAuditListener.class)
public class RecipeModel extends ItemModel{

    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(length = 500)
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


    //private List ingredients;
    //private List categories;
}

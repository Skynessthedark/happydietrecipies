package com.happydieting.dev.model;

import com.happydieting.dev.listener.IngredientAuditListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "INGREDIENT")
@EntityListeners(value = {IngredientAuditListener.class})
public class IngredientModel extends ItemModel{

    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "INGREDIENT2CATEGORY_REL",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
    private Set<CategoryModel> categories = new HashSet<>();
}

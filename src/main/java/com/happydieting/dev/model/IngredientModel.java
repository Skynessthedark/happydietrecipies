package com.happydieting.dev.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "INGREDIENT")
public class IngredientModel extends ItemModel{
    @Column(nullable = false)
    private String name;
}

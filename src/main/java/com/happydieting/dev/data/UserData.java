package com.happydieting.dev.data;

import com.happydieting.dev.model.CategoryModel;
import com.happydieting.dev.model.IngredientModel;
import com.happydieting.dev.model.NutritionUnitModel;
import com.happydieting.dev.model.NutritionalValueModel;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserData extends ItemData {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String repassword;
    private String bio;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] avatar;
}

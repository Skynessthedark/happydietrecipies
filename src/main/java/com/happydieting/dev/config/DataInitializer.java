package com.happydieting.dev.config;

import com.happydieting.dev.model.*;
import com.happydieting.dev.repository.*;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private SystemConfigRepository systemConfigRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private RecipeRepository recipeRepository;

    @Resource
    private NutritionUnitRepository nutritionUnitRepository;

    @Resource
    private IngredientRepository ingredientRepository;

    @Resource
    private CategoryRepository categoryRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String DATA_INITIALIZED_KEY = "data.initialized";
    @Autowired
    private NutritionTypeRepository nutritionTypeRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Optional<SystemConfigModel> config = systemConfigRepository.findByKey(DATA_INITIALIZED_KEY);
        if (config.isPresent() && Boolean.parseBoolean(config.get().getValue())) {
            return;
        }

        SystemConfigModel initializedConfig = new SystemConfigModel();
        initializedConfig.setKey(DATA_INITIALIZED_KEY);
        initializedConfig.setValue("true");
        initializedConfig.setDescription("Indicates whether initial data has been loaded");
        systemConfigRepository.save(initializedConfig);

        createUserData();
        createNutritions();
        createCategories();
        createIngredients();
        createRecipes();
        createNutritionalValues();
    }

    private void createUserData() {
        UserModel user1 = new UserModel();
        user1.setFullName("Test1 Kullanıcı");
        user1.setEmail("test1@example.com");
        user1.setUsername("test1@example.com");
        user1.setPassword(passwordEncoder.encode("password123"));
        user1.setBio("İlk test kullanıcısı");
        //TODO: user1.setAvatar(); mediamodel den sonra setlenecek
        entityManager.persist(user1);

        UserModel user2 = new UserModel();
        user2.setFullName("Test2 Kullanıcı");
        user2.setEmail("test2@example.com");
        user2.setUsername("test2@example.com");
        user2.setPassword(passwordEncoder.encode("password123"));
        user2.setBio("İkinci test kullanıcısı");
        //TODO: user2.setAvatar(); mediamodel den sonra setlenecek
        entityManager.persist(user2);
    }

    private void createNutritions() {
        createNutritionTypes();
        createNutritionUnits();
    }

    private void createNutritionTypes() {
        NutritionTypeModel protein = new NutritionTypeModel();
        protein.setCode("PROTEIN");
        protein.setName("Protein");
        entityManager.persist(protein);

        NutritionTypeModel sugar = new NutritionTypeModel();
        sugar.setCode("SUGAR");
        sugar.setName("Şeker(cho)");
        entityManager.persist(sugar);

        NutritionTypeModel oil = new NutritionTypeModel();
        oil.setCode("OIL");
        oil.setName("Yağ");
        entityManager.persist(oil);

        NutritionTypeModel cholesterol = new NutritionTypeModel();
        cholesterol.setCode("CHOLESTEROL");
        cholesterol.setName("Kolesterol");
        entityManager.persist(cholesterol);

        NutritionTypeModel calorie = new NutritionTypeModel();
        calorie.setCode("CALORIE");
        calorie.setName("Kalori");
        entityManager.persist(calorie);
    }

    private void createNutritionUnits() {
        NutritionUnitModel gr = new NutritionUnitModel();
        gr.setCode("GR");
        gr.setName("gr");
        entityManager.persist(gr);

        NutritionUnitModel portion = new NutritionUnitModel();
        portion.setCode("PORTION");
        portion.setName("Porsiyon");
        entityManager.persist(gr);

        NutritionUnitModel person = new NutritionUnitModel();
        person.setCode("PERSON");
        person.setName("Kişi");
        entityManager.persist(person);
    }

    private void createCategories() {
        CategoryModel mainCat = new CategoryModel();
        mainCat.setCode("HAPPY_DIET");
        mainCat.setName("Mutlu Diyet Tarifleri");
        entityManager.persist(mainCat);

        Set<CategoryModel> parentCategories = new HashSet<>();
        parentCategories.add(mainCat);

        CategoryModel cat1 = new CategoryModel();
        cat1.setCode("GLUTEN_FREE");
        cat1.setName("Glutensiz");
        cat1.setParentCategories(parentCategories);
        entityManager.persist(cat1);

        CategoryModel cat2 = new CategoryModel();
        cat2.setCode("VEGAN");
        cat2.setName("Vegan");
        cat2.setParentCategories(parentCategories);
        entityManager.persist(cat2);
    }

    private void createIngredients() {
        Set<CategoryModel> categories = new HashSet<>();
        categories.add(categoryRepository.findCategoryModelByCode("GLUTEN_FREE").get());
        categories.add(categoryRepository.findCategoryModelByCode("VEGAN").get());

        IngredientModel ingredient1 = new IngredientModel();
        ingredient1.setCode("RICE");
        ingredient1.setName("Pirinç");
        ingredient1.setCategories(categories);
        entityManager.persist(ingredient1);

        IngredientModel ingredient2 = new IngredientModel();
        ingredient1.setCode("PEANUT");
        ingredient2.setName("Fıstık");
        ingredient1.setCategories(categories);
        entityManager.persist(ingredient2);

        IngredientModel ingredient3 = new IngredientModel();
        ingredient1.setCode("HAZELNUT");
        ingredient3.setName("Fındık");
        ingredient1.setCategories(categories);
        entityManager.persist(ingredient3);
    }

    private void createRecipes() {
        Set<IngredientModel> ingredientModels = Set.copyOf(ingredientRepository.findAll());
        RecipeModel recipe1 = new RecipeModel();
        recipe1.setOwner(userRepository.findByUsername("test1@example.com").get());
        recipe1.setName("Test1 Recepti");
        recipe1.setDescription("Test1Tarif");
        recipe1.setTips("Örnek1");
        recipe1.setRecipe("Tariff");
        recipe1.setNutritionValue(100.0);
        recipe1.setNutritionUnit(nutritionUnitRepository.findNutritionUnitModelByCode("GR").get());
        recipe1.setIngredients(ingredientModels);

        Set<CategoryModel> categories1 = new HashSet<>();
        categories1.add(categoryRepository.findCategoryModelByCode("GLUTEN_FREE").get());
        categories1.add(categoryRepository.findCategoryModelByCode("VEGAN").get());

        recipe1.setCategories(categories1);
        entityManager.persist(recipe1);

        RecipeModel recipe2 = new RecipeModel();
        recipe2.setOwner(userRepository.findByUsername("test2@example.com").get());
        recipe2.setName("Test2 Recepti");
        recipe2.setDescription("Test2Tarif");
        recipe2.setTips("Örnek2");
        recipe2.setRecipe("Tariff");
        recipe2.setNutritionValue(1.0);
        recipe2.setNutritionUnit(nutritionUnitRepository.findNutritionUnitModelByCode("PERSON").get());
        recipe2.setIngredients(ingredientModels);

        Set<CategoryModel> categories2 = new HashSet<>();
        categories2.add(categoryRepository.findCategoryModelByCode("GLUTEN_FREE").get());

        recipe2.setCategories(categories2);
        entityManager.persist(recipe2);
    }

    private void createNutritionalValues() {
        NutritionTypeModel protein = nutritionTypeRepository.findNutritionTypeModelByCode("PROTEIN").get();
        NutritionTypeModel sugar = nutritionTypeRepository.findNutritionTypeModelByCode("SUGAR").get();
        NutritionUnitModel gr = nutritionUnitRepository.findNutritionUnitModelByCode("GR").get();

        RecipeModel recipe1 = recipeRepository.findRecipeModelById(1L).get();
        NutritionalValueModel protein1 = new NutritionalValueModel();
        protein1.setRecipe(recipe1);
        protein1.setType(protein);
        protein1.setUnit(gr);
        protein1.setValue(10.0);
        entityManager.persist(protein1);

        NutritionalValueModel sugar1 = new NutritionalValueModel();
        sugar1.setRecipe(recipe1);
        sugar1.setType(sugar);
        sugar1.setUnit(gr);
        sugar1.setValue(5.0);
        entityManager.persist(sugar1);

        RecipeModel recipe2 = recipeRepository.findRecipeModelById(2L).get();
        NutritionalValueModel protein2 = new NutritionalValueModel();
        protein2.setRecipe(recipe2);
        protein2.setType(protein);
        protein2.setUnit(gr);
        protein2.setValue(15.0);
        entityManager.persist(protein2);

        NutritionalValueModel sugar2 = new NutritionalValueModel();
        sugar2.setRecipe(recipe2);
        sugar2.setType(sugar);
        sugar2.setUnit(gr);
        sugar2.setValue(7.0);
        entityManager.persist(sugar2);
    }

} 
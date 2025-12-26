const RECIPE_FORM = 'recipeForm';

const ingredientPlugin = new IngredientSelector({
    searchInputId: 'modalIngredientSearch',
    resultContainerId: 'ingredientSearchResults',
    selectedContainerId: 'selectedIngredients',
    hiddenInputId: 'modalIngredients',
    searchUrl: '/ingredients/search?ingredientName='
});

// Initialize when document is ready
document.addEventListener('DOMContentLoaded', function() {

    document.getElementById(RECIPE_FORM).addEventListener('submit', function(e) {
        validateForm(RECIPE_FORM);
    });

    ingredientPlugin.init();

});
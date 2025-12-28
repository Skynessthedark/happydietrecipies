const RECIPE_FORM = 'recipeForm';

const ingredientPlugin = new SearchSelector({
    searchInputId: 'modalIngredientSearch',
    resultContainerId: 'ingredientSearchResults',
    selectedContainerId: 'selectedIngredients',
    hiddenInputId: 'modalIngredients',
    searchUrl: '/ingredient/search?ingredientName='
});
const categoryPlugin = new SearchSelector({
    searchInputId: 'modalCategorySearch',
    resultContainerId: 'categorySearchResults',
    selectedContainerId: 'selectedCategories',
    hiddenInputId: 'modalCategories',
    searchUrl: '/category/search?categoryName='
});

const recipeImagePlugin = new ImageUploader({
    uploadAreaId: 'recipeImageArea',
    previewImgId: 'recipePreviewImg',
    previewDivId: 'recipeImagePreview',
    inputFileId: 'recipeFile',
    removeBtnId: 'removeImageBtn'
});

// Initialize when document is ready
document.addEventListener('DOMContentLoaded', function() {

    document.getElementById(RECIPE_FORM).addEventListener('submit', function(e) {
        validateForm(RECIPE_FORM);
    });

    categoryPlugin.init();
    ingredientPlugin.init();
    recipeImagePlugin.init();

});
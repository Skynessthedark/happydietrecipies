const RECIPE_FORM = 'recipeForm';

const ingredientPlugin = new SearchSelector({
    searchInputId: 'modalIngredientSearch',
    resultContainerId: 'ingredientSearchResults',
    selectedContainerId: 'selectedIngredients',
    hiddenInputId: 'ingredientCodes',
    searchUrl: '/ingredient/search?ingredientName='
});
const categoryPlugin = new SearchSelector({
    searchInputId: 'modalCategorySearch',
    resultContainerId: 'categorySearchResults',
    selectedContainerId: 'selectedCategories',
    hiddenInputId: 'categoryCodes',
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

    categoryPlugin.init();
    ingredientPlugin.init();
    recipeImagePlugin.init();

    document.getElementById(RECIPE_FORM).addEventListener('submit', function(e) {
        e.preventDefault();
        if(validateForm(RECIPE_FORM)){
            return false;
        }

        let formData = new FormData(this);

        let categoryValues = document.getElementById('categoryCodes').value.split(',');
        const categories = categoryValues.map(categoryValue => {
        return {
            code: categoryValue
        }});
        formData.append('categories', JSON.stringify(categories));

        let ingredientValues = document.getElementById('ingredientCodes').value.split(',');
        const ingredients = ingredientValues.map(ingredientValue => {
            return {
                code: ingredientValue
            }});
        formData.append('ingredients', JSON.stringify(ingredients));

        let servingUnitValue = document.getElementById('servingUnit').value;
        const servingUnit = {
            code: servingUnitValue
        };
        formData.append('servingUnit', JSON.stringify(servingUnit));

        let nutritionTypeEls = document.querySelectorAll('.nutritionType');
        let nutritionalValues = [];
        nutritionTypeEls.forEach(nutritionTypeEl => {
            const nutritionType = nutritionTypeEl.value;
            const valId = 'nutrition-value-' + nutritionType
            const val = document.getElementById(valId).value;
            if(nutritionType !== ''){
                nutritionalValues.push({
                    nutritionType: nutritionType,
                    value: val,
                    //nutritionUnit: servingUnit
                });
            }
        });
        formData.append('nutritionalValues', JSON.stringify(nutritionalValues));

        var url = this.getAttribute('action');
        return sendRecipeForm(formData, url);
    });

});

function sendRecipeForm(formData, url) {
    let blobForm = new FormData();
    blobForm.append("recipeForm", new Blob([JSON.stringify(formData)], { type: "application/json" }));
    blobForm.append('image', document.getElementById('recipeFile').files[0]);
    $.ajax({
        type: "POST",
        url: url,
        data: blobForm,
        cache: false,
        async: false,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function (data) {
            if(data !== null && data.success === true){
                window.location.href = "/";
                return true;
            }
            else{
                alert("Something went wrong. Please try again.");
                return false;
            }
        },
        error: function (e) {
            console.log(e);
            return false;
        }
    });
}
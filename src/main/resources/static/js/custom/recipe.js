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

        let formObject = {};
        formData.forEach((value, key) => {
            formObject[key] = value;
        });

        let categoryValues = document.getElementById('categoryCodes').value.split(',');
        formObject['categories'] = categoryValues.map(categoryValue => {
            return {
                code: categoryValue
            }
        });

        let ingredientValues = document.getElementById('ingredientCodes').value.split(',');
        formObject['ingredients'] = ingredientValues.map(ingredientValue => {
            return {
                code: ingredientValue
            }
        });

        let servingUnitValue = document.getElementById('servingUnit').value;
        formObject['servingUnit'] = {
            code: servingUnitValue
        };

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
        formObject['nutritionalValues'] = nutritionalValues;

        let url = this.getAttribute('action');
        return sendRecipeForm(formObject, url);
    });

});

function sendRecipeForm(formObject, url) {
    let blobForm = new FormData();
    blobForm.append("recipeForm", new Blob([JSON.stringify(formObject)], { type: "application/json" }));
    blobForm.append('image', document.getElementById('recipeFile').files[0]);
    $.ajax({
        type: "POST",
        url: SERVER_PATH + url,
        data: blobForm,
        cache: false,
        async: false,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function (data) {
            if(data !== null && data.status === true){
                window.location.href = SERVER_PATH + "/";
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
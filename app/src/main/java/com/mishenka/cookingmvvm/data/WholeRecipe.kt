package com.mishenka.cookingmvvm.data

data class WholeRecipe (
        var key: String? = null,
        var ingredientsList: List<Ingredient>? = null,
        var stepsList: List<FirebaseStep>? = null
)
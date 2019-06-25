package com.mishenka.cookingmvvm.data.source

import com.mishenka.cookingmvvm.data.CombinedRecipe
import com.mishenka.cookingmvvm.data.Recipe
import com.mishenka.cookingmvvm.data.WholeRecipe

interface RecipeDataSource {

    interface LoadRecipesCallback {
        fun onRecipesLoaded(recipes: List<Recipe>)
        fun onDataNotAvailable()
    }

    interface GetCombinedRecipeCallback {
        fun onRecipeLoaded(combinedRecipe: CombinedRecipe)
        fun onDataNotAvailable()
    }

    interface GetTotalPostsNumberCallback {
        fun onTotalPostsNumberGot(totalPosts: Long)
        fun onDataNotAvailable()
    }

    interface GetTotalViewsCountCallback {
        fun onTotalViewCountGot(totalViews: Long)
        fun onDataNotAvailable()
    }

    interface GetWholeRecipeCallback {
        fun onWholeRecipeLoaded(recipe: Recipe, wholeRecipe: WholeRecipe)
        fun onDataNotAvailable()
    }

    interface SaveRecipeCallback {
        fun onRecipeSaved(recipe: Recipe)
        fun onDataNotAvailable()
    }

    fun refreshRecipes()

    fun getTotalPostsNumber(userUID: String, callback: GetTotalPostsNumberCallback)

    fun getTotalViewsCount(userUID: String, callback: GetTotalViewsCountCallback)

    fun incrementReadCount(recipeId: String)

    fun loadRecipes(callback: LoadRecipesCallback)

    fun getCombinedRecipe(recipeId: String, callback: GetCombinedRecipeCallback)

    fun getWholeRecipe(recipeId: String, callback: GetWholeRecipeCallback)

    fun saveCombinedRecipe(combinedRecipe: CombinedRecipe, callback: SaveRecipeCallback): String?

}
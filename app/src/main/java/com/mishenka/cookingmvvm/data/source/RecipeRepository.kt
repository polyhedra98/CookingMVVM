package com.mishenka.cookingmvvm.data.source

import com.mishenka.cookingmvvm.data.CombinedRecipe
import com.mishenka.cookingmvvm.data.source.remote.RecipeRemoteDataSource

class RecipeRepository(
        private val recipeRemoteDataSource: RecipeRemoteDataSource
) : RecipeDataSource {

    var cachedRecipes: LinkedHashMap<String, CombinedRecipe> = LinkedHashMap()
    var cacheIsDirty = false

    override fun loadRecipes(callback: RecipeDataSource.LoadRecipesCallback) {
        recipeRemoteDataSource.loadRecipes(callback)
    }

    override fun getCombinedRecipe(recipeId: String, callback: RecipeDataSource.GetCombinedRecipeCallback) {
        recipeRemoteDataSource.getCombinedRecipe(recipeId, callback)
    }

    override fun getWholeRecipe(recipeId: String, callback: RecipeDataSource.GetWholeRecipeCallback) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveCombinedRecipe(combinedRecipe: CombinedRecipe, callback: RecipeDataSource.SaveRecipeCallback): String? {
        recipeRemoteDataSource.saveCombinedRecipe(combinedRecipe, callback)
        return "nya"
    }

    override fun getTotalPostsNumber(userUID: String, callback: RecipeDataSource.GetTotalPostsNumberCallback) {
        recipeRemoteDataSource.getTotalPostsNumber(userUID, callback)
    }

    override fun getTotalViewsCount(userUID: String, callback: RecipeDataSource.GetTotalViewsCountCallback) {
        recipeRemoteDataSource.getTotalViewsCount(userUID, callback)
    }

    override fun incrementReadCount(recipeId: String) {
        recipeRemoteDataSource.incrementReadCount(recipeId)
    }

    override fun refreshRecipes() {
        cacheIsDirty = true
    }

    private fun refreshCache(combinedRecipe: List<CombinedRecipe>) {
        cachedRecipes.clear()
        combinedRecipe.forEach {
            performCaching(it) { performingRecipe ->
                val recipeToCache = CombinedRecipe(performingRecipe.recipe, performingRecipe.wholeRecipe)
                cachedRecipes[recipeToCache.recipe.key!!] = recipeToCache
            }
        }
        cacheIsDirty = false
    }

    private inline fun performCaching(combinedRecipe: CombinedRecipe, perform: (CombinedRecipe) -> Unit) {
        perform(combinedRecipe)
    }

    companion object {
        private var INSTANCE: RecipeRepository? = null

        @JvmStatic fun getInstance(recipeRemoteDataSource: RecipeRemoteDataSource) =
                INSTANCE ?: synchronized(RecipeRepository::class.java) {
                    INSTANCE ?: RecipeRepository(recipeRemoteDataSource).also { INSTANCE = it }
                }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}
package com.mishenka.cookingmvvm.detail

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishenka.cookingmvvm.data.CombinedRecipe
import com.mishenka.cookingmvvm.data.FirebaseStep
import com.mishenka.cookingmvvm.data.Ingredient
import com.mishenka.cookingmvvm.data.Step
import com.mishenka.cookingmvvm.data.source.RecipeDataSource
import com.mishenka.cookingmvvm.data.source.RecipeRepository

class DetailViewModel(
        private val recipeRepository: RecipeRepository
) : ViewModel() {

    val recipeName: LiveData<String>
        get() = _recipeName
    private val _recipeName = MutableLiveData<String>()

    val recipeDescription: LiveData<String>
        get() = _recipeDescription
    private val _recipeDescription = MutableLiveData<String>()

    val recipeAuthor: LiveData<String>
        get() = _recipeAuthor
    private val _recipeAuthor = MutableLiveData<String>()

    val recipeMainPic: LiveData<String>
        get() = _recipeMainPic
    private val _recipeMainPic = MutableLiveData<String>()

    val ingredientsList: LiveData<List<Ingredient?>>
        get() = _ingredientsList
    private val _ingredientsList = MutableLiveData<List<Ingredient?>>()

    val ingredientsVisibility: LiveData<Int>
        get() = _ingredientsVisibility
    private val _ingredientsVisibility = MutableLiveData<Int>()

    val stepsList: LiveData<List<FirebaseStep?>>
        get() = _stepsList
    private val _stepsList = MutableLiveData<List<FirebaseStep?>>()

    val stepsVisibility: LiveData<Int>
        get() = _stepsVisibility
    private val _stepsVisibility = MutableLiveData<Int>()

    fun start(recipeKey: String) {
        recipeRepository.getCombinedRecipe(recipeKey, object : RecipeDataSource.GetCombinedRecipeCallback {
            override fun onRecipeLoaded(combinedRecipe: CombinedRecipe) {
                initializeCombinedRecipe(combinedRecipe)
            }

            override fun onDataNotAvailable() {
                Log.i("NYA", "Data not available *DetailViewModel*")
            }
        })
        recipeRepository.incrementReadCount(recipeKey)
    }

    private fun initializeCombinedRecipe(combinedRecipe: CombinedRecipe) {
        _recipeName.value = combinedRecipe.recipe.name
        _recipeAuthor.value = combinedRecipe.recipe.author
        _recipeDescription.value = combinedRecipe.recipe.description
        _recipeMainPic.value = combinedRecipe.recipe.mainPicUrl

        _ingredientsList.value = combinedRecipe.wholeRecipe.ingredientsList
        _ingredientsVisibility.value = if (ingredientsList.value.isNullOrEmpty()) { View.GONE } else { View.VISIBLE }

        _stepsList.value = combinedRecipe.wholeRecipe.stepsList
        _stepsVisibility.value = if (stepsList.value.isNullOrEmpty()) { View.GONE } else { View.VISIBLE }
    }

}
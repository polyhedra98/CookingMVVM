package com.mishenka.cookingmvvm.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishenka.cookingmvvm.data.Recipe
import com.mishenka.cookingmvvm.data.source.RecipeDataSource
import com.mishenka.cookingmvvm.data.source.RecipeRepository
import com.mishenka.cookingmvvm.util.Event

class HomeViewModel(
        private val recipeRepository: RecipeRepository
) : ViewModel() {

    val items: LiveData<List<Recipe>>
        get() = _items
    private val _items = MutableLiveData<List<Recipe>>().apply { value = emptyList() }

    val recipeClickedId: LiveData<String>
        get() = _recipeClickedId
    private val _recipeClickedId = MutableLiveData<String>()

    val recipeClickedEvent: LiveData<Event<Unit>>
        get() = _recipeClickedEvent
    private val _recipeClickedEvent = MutableLiveData<Event<Unit>>()

    val starClickedEvent: LiveData<Event<Unit>>
        get() = _starClickedEvent
    private val _starClickedEvent = MutableLiveData<Event<Unit>>()

    fun start() {
        loadRecipes()
    }

    private fun loadRecipes() {
        recipeRepository.loadRecipes(object : RecipeDataSource.LoadRecipesCallback {
            override fun onRecipesLoaded(recipes: List<Recipe>) {
                _items.value = recipes
            }

            override fun onDataNotAvailable() {
                Log.i("NYA", "loadRecipes onDataNotAvailable in HomeViewModel")
            }
        })
    }

    internal fun starClicked(recipeId: String) {
        _recipeClickedId.value = recipeId
        _starClickedEvent.value = Event(Unit)
    }

    internal fun recipeClicked(recipeId: String) {
        _recipeClickedId.value = recipeId
        _recipeClickedEvent.value = Event(Unit)
    }
}

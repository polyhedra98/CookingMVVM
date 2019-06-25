package com.mishenka.cookingmvvm.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.util.VisibleForTesting
import com.mishenka.cookingmvvm.addrecipe.AddRecipeViewModel
import com.mishenka.cookingmvvm.data.source.RecipeRepository
import com.mishenka.cookingmvvm.detail.DetailViewModel
import com.mishenka.cookingmvvm.home.HomeViewModel
import com.mishenka.cookingmvvm.me.MeViewModel

class ViewModelFactory private constructor(
        private val recipeRepository: RecipeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(HomeViewModel::class.java) ->
                        HomeViewModel(recipeRepository)
                    isAssignableFrom(DetailViewModel::class.java) ->
                        DetailViewModel(recipeRepository)
                    isAssignableFrom(MeViewModel::class.java) ->
                        MeViewModel(recipeRepository)
                    isAssignableFrom(AddRecipeViewModel::class.java) ->
                        AddRecipeViewModel(recipeRepository)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(
                            Injection.provideRecipeRepository(context)
                    ).also { INSTANCE = it }
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
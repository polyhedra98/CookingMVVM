package com.mishenka.cookingmvvm.data

import android.util.Log

class EditableIngredientWrapper(
        val ingredient: Ingredient,
        val index: Int
) {
    fun onTextChanged(newText: CharSequence) {
        ingredient.text = newText.toString()
    }
}
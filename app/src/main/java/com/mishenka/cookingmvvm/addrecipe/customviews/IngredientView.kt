package com.mishenka.cookingmvvm.addrecipe.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mishenka.cookingmvvm.addrecipe.AddRecipeViewModel
import com.mishenka.cookingmvvm.data.EditableIngredientWrapper
import com.mishenka.cookingmvvm.data.Ingredient
import com.mishenka.cookingmvvm.databinding.ItemIngredientBinding

@SuppressLint("ViewConstructor")
class IngredientView : LinearLayout {

    constructor(editableIngredient: EditableIngredientWrapper, context: Context) : super(context) {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ItemIngredientBinding.inflate(inflater, this, true)
        viewBinding.editableIngredient = editableIngredient
    }

    constructor(editableIngredient: EditableIngredientWrapper, context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ItemIngredientBinding.inflate(inflater, this, true)
        viewBinding.editableIngredient = editableIngredient
    }

    constructor(editableIngredient: EditableIngredientWrapper, context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ItemIngredientBinding.inflate(inflater, this, true)
        viewBinding.editableIngredient = editableIngredient
    }
}
package com.mishenka.cookingmvvm.detail.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mishenka.cookingmvvm.R
import com.mishenka.cookingmvvm.data.Ingredient
import com.mishenka.cookingmvvm.databinding.ItemNonInteractiveIngredientBinding
import com.mishenka.cookingmvvm.detail.DetailViewModel


@SuppressLint("ViewConstructor")
class NonInteractiveIngredientView : LinearLayout {
    constructor(ingredient: Ingredient, context: Context) : super(context) {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ItemNonInteractiveIngredientBinding.inflate(inflater, this, true)
        viewBinding.ingredient = ingredient
    }
    constructor(ingredient: Ingredient, context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ItemNonInteractiveIngredientBinding.inflate(inflater, this, true)
        viewBinding.ingredient = ingredient
    }
    constructor(ingredient: Ingredient, context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ItemNonInteractiveIngredientBinding.inflate(inflater, this, true)
        viewBinding.ingredient = ingredient
    }
}
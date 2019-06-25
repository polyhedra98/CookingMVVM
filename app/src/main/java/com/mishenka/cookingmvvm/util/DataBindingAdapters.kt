package com.mishenka.cookingmvvm.util

import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mishenka.cookingmvvm.addrecipe.customviews.IngredientView
import com.mishenka.cookingmvvm.data.*
import com.mishenka.cookingmvvm.detail.customviews.NonInteractiveIngredientView
import com.mishenka.cookingmvvm.detail.customviews.NonInteractiveStepView
import com.mishenka.cookingmvvm.home.RecipesAdapter

@BindingAdapter("app:imageUri")
fun setImageUri(view: ImageView, uri: Uri?) {
    Log.i("NYA", "From adapter. Setting uri: $uri")
    if (uri != null) {
        view.visibility = View.VISIBLE
        Glide.with(view.context)
                .load(uri)
                .apply(RequestOptions().centerCrop())
                .into(view)

    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:imageUri")
fun setImageUri(view: ImageView, stringUri: String?) {
    Log.i("NYA", "From adapter. Setting stringUri: $stringUri")
    if (!stringUri.isNullOrBlank() && !stringUri.isNullOrEmpty()) {
        view.visibility = View.VISIBLE
        val uri = Uri.parse(stringUri)
        Glide.with(view.context)
                .load(uri)
                .apply(RequestOptions().centerCrop())
                .into(view)
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:ingredients")
fun setIngredients(viewGroup: ViewGroup, ingredients: List<Ingredient?>?) {
    ingredients?.let { safeIngredients ->
        for (ingredient in safeIngredients) {
            ingredient?.let { safeIngredient ->
                viewGroup.addView(NonInteractiveIngredientView(safeIngredient, viewGroup.context))
            }
        }
    }
}

@BindingAdapter("app:editableIngredients")
fun setInteractableIngredients(viewGroup: ViewGroup, editableIngredients: List<EditableIngredientWrapper?>?) {
    viewGroup.removeAllViews()
    editableIngredients?.let { safeEditableIngredients ->
        for (editableIngredient in safeEditableIngredients) {
            editableIngredient?.let { safeEditableIngredient ->
                viewGroup.addView(IngredientView(safeEditableIngredient, viewGroup.context))
            }
        }
    }
}

@BindingAdapter("app:steps")
fun setSteps(viewGroup: ViewGroup, steps: List<FirebaseStep?>?) {
    steps?.let { safeSteps ->
        for (step in safeSteps) {
            step?.let { safeStep ->
                viewGroup.addView(NonInteractiveStepView(safeStep, viewGroup.context))
            }
        }

    }
    steps?.let {

    }
}

@BindingAdapter("app:stepImages")
fun setImages(viewGroup: ViewGroup, picUrls: List<String?>?) {
    picUrls?.let { safePicUrls ->
        for (stepPic in safePicUrls) {
            stepPic?.let { safeStepPic ->
                val layoutParams = LinearLayout.LayoutParams(0, 250)
                layoutParams.weight = 1.toFloat()
                layoutParams.setMargins(4, 0, 4, 0)
                viewGroup.addView(ImageView(viewGroup.context).apply { this.layoutParams = layoutParams }.also { imageView ->
                    Glide.with(viewGroup.context)
                            .load(Uri.parse(safeStepPic))
                            .apply(RequestOptions().centerCrop())
                            .into(imageView)
                })
            }
        }
    }
}

@BindingAdapter("app:longText")
fun setText(view: TextView, longText: Long) {
    view.text = longText.toString()
}

@BindingAdapter("app:recipeItems")
fun setItems(listView: ListView, items: List<Recipe>) {
    with(listView.adapter as RecipesAdapter) {
        replaceData(items)
    }
}
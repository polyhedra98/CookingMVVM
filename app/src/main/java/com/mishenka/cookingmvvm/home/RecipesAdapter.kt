package com.mishenka.cookingmvvm.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.mishenka.cookingmvvm.data.Recipe
import com.mishenka.cookingmvvm.databinding.ItemRecipeBinding
import com.mishenka.cookingmvvm.home.interfaces.RecipeClickListener
import java.lang.IllegalStateException

class RecipesAdapter(
        private var recipes: List<Recipe>,
        private val viewModel: HomeViewModel
) : BaseAdapter() {

    fun replaceData(recipes: List<Recipe>) {
        setList(recipes)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemRecipeBinding
        binding = if (convertView == null) {
            val inflater = LayoutInflater.from(parent.context)
            ItemRecipeBinding.inflate(inflater, parent, false)
        } else {
            DataBindingUtil.getBinding(convertView) ?: throw IllegalStateException()
        }

        val recipeClickListener = object : RecipeClickListener {
            override fun recipeClicked() {
                Log.i("NYA", "Recipe $position clicked")
                viewModel.recipeClicked(recipes[position].key!!)
            }

            override fun starClicked() {
                Log.i("NYA", "Star $position clicked")
                viewModel.starClicked(recipes[position].key!!)
            }
        }

        with(binding) {
            binding.upperRecipe.root.setOnClickListener { recipeClickListener.recipeClicked() }
            recipe = recipes[position]
            clickListener = recipeClickListener
            executePendingBindings()
        }
        return binding.root
    }

    override fun getItem(position: Int) = recipes[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = recipes.size

    private fun setList(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }
}
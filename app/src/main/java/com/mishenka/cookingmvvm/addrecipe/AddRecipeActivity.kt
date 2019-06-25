package com.mishenka.cookingmvvm.addrecipe

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.mishenka.cookingmvvm.R
import com.mishenka.cookingmvvm.util.*

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var viewModel: AddRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipe_activity)

        setupActionBar(R.id.add_recipe_toolbar) {
            setDisplayHomeAsUpEnabled(true)
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.add_recipe_content_frame)
            if (currentFragment != null) {
                replaceFragmentInActivity(DeniedAddRecipeFragment.newInstance(), R.id.add_recipe_content_frame)
            } else {
                addFragmentToActivity(DeniedAddRecipeFragment.newInstance(), R.id.add_recipe_content_frame)
            }
        } else {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.add_recipe_content_frame)
            if (currentFragment != null) {
                replaceFragmentInActivity(AddRecipeFragment.newInstance(), R.id.add_recipe_content_frame)
            } else {
                addFragmentToActivity(AddRecipeFragment.newInstance(), R.id.add_recipe_content_frame)
            }
        }
    }

}
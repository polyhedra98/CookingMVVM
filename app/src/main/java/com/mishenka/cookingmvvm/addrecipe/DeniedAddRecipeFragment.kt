package com.mishenka.cookingmvvm.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mishenka.cookingmvvm.R

class DeniedAddRecipeFragment : Fragment() {

    companion object {
        fun newInstance() = DeniedAddRecipeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.denied_add_recipe_fragment, container, false)
    }

}
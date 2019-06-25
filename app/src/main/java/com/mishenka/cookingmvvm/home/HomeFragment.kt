package com.mishenka.cookingmvvm.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.mishenka.cookingmvvm.R
import com.mishenka.cookingmvvm.databinding.HomeFragmentBinding
import com.mishenka.cookingmvvm.detail.DetailActivity
import com.mishenka.cookingmvvm.util.Event
import com.mishenka.cookingmvvm.util.Utils
import com.mishenka.cookingmvvm.util.obtainViewModel

class HomeFragment : Fragment() {

    private lateinit var viewDataBinding: HomeFragmentBinding
    private lateinit var listAdapter: RecipesAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.viewModel?.start()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel().apply {
            recipeClickedEvent.observe(this@HomeFragment, Observer<Event<Unit>> { event ->
                event.getContentIfNotHandled()?.let {
                    observeRecipeClick(viewModel.recipeClickedId.value!!)
                }
            })

            starClickedEvent.observe(this@HomeFragment, Observer<Event<Unit>> { event ->
                event.getContentIfNotHandled()?.let {
                    observeStarClick(viewModel.recipeClickedId.value!!)
                }
            })
        }
        viewDataBinding.setLifecycleOwner(this.viewLifecycleOwner)
        viewDataBinding.viewModel = viewModel
        setupListAdapter()
    }

    private fun observeRecipeClick(recipeId: String) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(Utils.INTENT_RECIPE_KEY_KEY, recipeId)
        }
        startActivity(intent)
    }

    private fun observeStarClick(recipeId: String) {

    }

    private fun setupListAdapter() {
        listAdapter = RecipesAdapter(ArrayList(0), viewModel)
        viewDataBinding.homeRecipesList.adapter = listAdapter
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    private fun obtainViewModel() = obtainViewModel(HomeViewModel::class.java)
}
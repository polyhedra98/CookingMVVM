package com.mishenka.cookingmvvm.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mishenka.cookingmvvm.R
import com.mishenka.cookingmvvm.databinding.DetailFragmentBinding
import com.mishenka.cookingmvvm.util.Utils
import com.mishenka.cookingmvvm.util.obtainViewModel

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewDataBinding: DetailFragmentBinding
    private lateinit var viewModel: DetailViewModel
    private var recipeKey: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment, container, false)
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recipeKey = activity?.intent?.extras?.getString(Utils.INTENT_RECIPE_KEY_KEY)
        viewModel = obtainViewModel().apply {
            // TODO("Observations *probably none*")
        }
        viewDataBinding.setLifecycleOwner(this)
        viewDataBinding.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        if (!recipeKey.isNullOrBlank()) {
            viewDataBinding.viewModel?.start(recipeKey!!)
        }
    }

    private fun obtainViewModel() = obtainViewModel(DetailViewModel::class.java)
}
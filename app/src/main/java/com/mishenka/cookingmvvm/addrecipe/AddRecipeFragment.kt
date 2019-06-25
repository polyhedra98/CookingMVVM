package com.mishenka.cookingmvvm.addrecipe

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mishenka.cookingmvvm.R
import com.mishenka.cookingmvvm.databinding.AddRecipeFragmentBinding
import com.mishenka.cookingmvvm.util.Event
import com.mishenka.cookingmvvm.util.Utils
import com.mishenka.cookingmvvm.util.obtainViewModel

class AddRecipeFragment : Fragment() {

    companion object {
        fun newInstance() = AddRecipeFragment()
    }

    private lateinit var viewDataBinding: AddRecipeFragmentBinding
    private lateinit var viewModel: AddRecipeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.add_recipe_fragment, container, false)
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel().apply {
            submitButtonEvent.observe(this@AddRecipeFragment, Observer<Event<Unit>> { event ->
                event.getContentIfNotHandled()?.let {
                    observeSubmit()
                }
            })

            stepPicEvent.observe(this@AddRecipeFragment, Observer<Event<Unit>> { event ->
                event.getContentIfNotHandled()?.let {
                    observeStepButtonEvent()
                }
            })

            mainPictureButtonEvent.observe(this@AddRecipeFragment, Observer<Event<Unit>> { event ->
                event.getContentIfNotHandled()?.let {
                    observeMainPicButtonEvent()
                }
            })
        }
        viewDataBinding.setLifecycleOwner(this)
        viewDataBinding.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.viewModel?.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        when (requestCode) {
            Utils.STEP_GALLERY -> {
                val stepPicUri = data?.data
                stepPicUri?.let { safeStepPicUri ->
                    viewDataBinding.viewModel?.updateStepPicture(safeStepPicUri)
                }
            }
            Utils.MAIN_GALLERY -> {
                val mainPicUri = data?.data
                mainPicUri?.let { safeMainPicUri ->
                    viewDataBinding.viewModel?.updateMainPicture(safeMainPicUri)
                }
            }
        }
    }

    private fun observeStepButtonEvent() {
        Log.i("NYA", "Step button event observed in AddRecipeFragment")
        showPictureDialog(Utils.STEP_GALLERY)
    }

    private fun observeMainPicButtonEvent() {
        Log.i("NYA", "Main pic button event observed in AddRecipeFragment")
        showPictureDialog(Utils.MAIN_GALLERY)
    }

    private fun observeSubmit() {
        Log.i("NYA", "Submit observed in AddRecipeFragment")
        activity?.finish()
    }

    private fun showPictureDialog(RC_CODE_GALLERY: Int) {
        val pictureDialog = AlertDialog.Builder(this.context)
        pictureDialog.setTitle("Select action")
        val pictureDialogItems = arrayOf("Gallery")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallery(RC_CODE_GALLERY)
                else -> Log.i("NYA", "Unknown stepPictureDialog RC")
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery(RC_CODE: Int) {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, RC_CODE)
    }

    private fun obtainViewModel(): AddRecipeViewModel = obtainViewModel(AddRecipeViewModel::class.java)
}
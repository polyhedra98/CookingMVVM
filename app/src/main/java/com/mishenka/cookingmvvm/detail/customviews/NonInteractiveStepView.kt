package com.mishenka.cookingmvvm.detail.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mishenka.cookingmvvm.data.FirebaseStep
import com.mishenka.cookingmvvm.databinding.ItemNonInteractiveStepBinding

@SuppressLint("ViewConstructor")
class NonInteractiveStepView : LinearLayout {
    constructor(step: FirebaseStep, context: Context) : super(context) {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ItemNonInteractiveStepBinding.inflate(inflater, this, true)
        viewBinding.step = step
    }
    constructor(step: FirebaseStep, context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ItemNonInteractiveStepBinding.inflate(inflater, this, true)
        viewBinding.step = step
    }
    constructor(step: FirebaseStep, context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ItemNonInteractiveStepBinding.inflate(inflater, this, true)
        viewBinding.step = step
    }
}
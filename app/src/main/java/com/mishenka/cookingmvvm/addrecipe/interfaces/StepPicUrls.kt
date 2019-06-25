package com.mishenka.cookingmvvm.addrecipe.interfaces

import androidx.lifecycle.LiveData

interface StepPicUrls {
    val firstPicUrl: LiveData<String>
    val secondPicUrl: LiveData<String>
    val thirdPicUrl: LiveData<String>
}
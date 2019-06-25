package com.mishenka.cookingmvvm.util

import androidx.multidex.MultiDexApplication

class MainApplication : MultiDexApplication() {
    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        val applicationContext
            get() = instance!!.applicationContext
    }
}
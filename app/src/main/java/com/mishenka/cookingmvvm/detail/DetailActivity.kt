package com.mishenka.cookingmvvm.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mishenka.cookingmvvm.R
import com.mishenka.cookingmvvm.util.replaceFragmentInActivity
import com.mishenka.cookingmvvm.util.setupActionBar

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setupActionBar(R.id.detail_toolbar) {
            setDisplayHomeAsUpEnabled(true)
        }

        supportFragmentManager.findFragmentById(R.id.detail_content_frame)
                ?: replaceFragmentInActivity(DetailFragment.newInstance(), R.id.detail_content_frame)
    }

}
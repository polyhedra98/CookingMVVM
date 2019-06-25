package com.mishenka.cookingmvvm.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mishenka.cookingmvvm.R
import com.mishenka.cookingmvvm.addrecipe.AddRecipeActivity
import com.mishenka.cookingmvvm.me.MeFragment
import com.mishenka.cookingmvvm.util.replaceFragmentInActivity
import com.mishenka.cookingmvvm.util.setupActionBar

class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var prevCheckedNavigationItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        setupActionBar(R.id.home_toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px)
            setDisplayHomeAsUpEnabled(true)
        }

        setupNavigationDrawer()
        supportFragmentManager.findFragmentById(R.id.home_content_frame)
                ?: replaceFragmentInActivity(HomeFragment.newInstance(), R.id.home_content_frame)
    }


    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private fun setupNavigationDrawer() {
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout))
                .apply {
                    setStatusBarBackground(R.color.colorPrimaryDark)
                }
        setupDrawerContent(findViewById(R.id.nav_view))
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        prevCheckedNavigationItem = navigationView.menu.findItem(R.id.home_navigation_menu_item)
        prevCheckedNavigationItem.isChecked = true
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_navigation_menu_item -> {
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.home_content_frame)
                    if (currentFragment !is HomeFragment) {
                        prevCheckedNavigationItem.isChecked = false
                        prevCheckedNavigationItem = menuItem
                        replaceFragmentInActivity(HomeFragment.newInstance(), R.id.home_content_frame)
                        menuItem.isChecked = true
                    }
                }
                R.id.add_recipe_navigation_menu_item -> {
                    val intent = Intent(this, AddRecipeActivity::class.java)
                    startActivity(intent)
                }
                R.id.me_navigation_menu_item -> {
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.home_content_frame)
                    if (currentFragment !is MeFragment) {
                        prevCheckedNavigationItem.isChecked = false
                        prevCheckedNavigationItem = menuItem
                        replaceFragmentInActivity(MeFragment.newInstance(), R.id.home_content_frame)
                        menuItem.isChecked = true
                    }
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}

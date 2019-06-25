package com.mishenka.cookingmvvm.me

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.mishenka.cookingmvvm.data.source.RecipeDataSource
import com.mishenka.cookingmvvm.data.source.RecipeRepository
import com.mishenka.cookingmvvm.util.Event

class MeViewModel(
        private val recipeRepository: RecipeRepository
) : ViewModel() {

    val elementsVisibility: LiveData<Int>
        get() = _elementsVisibility
    private val _elementsVisibility = MutableLiveData<Int>()

    val signInButtonVisibility: LiveData<Int>
        get() = _signInButtonVisibility
    private val _signInButtonVisibility = MutableLiveData<Int>()

    val signOutButtonVisibility: LiveData<Int>
        get() = _signOutButtonVisibility
    private val _signOutButtonVisibility = MutableLiveData<Int>()

    val signInEvent: LiveData<Event<Unit>>
        get() = _signInEvent
    private val _signInEvent = MutableLiveData<Event<Unit>>()

    val signOutEvent: LiveData<Event<Unit>>
        get() = _signOutEvent
    private val _signOutEvent = MutableLiveData<Event<Unit>>()

    val username: LiveData<String>
        get() = _username
    private val _username = MutableLiveData<String>()

    val totalPosts: LiveData<String>
        get() = _totalPosts
    private val _totalPosts = MutableLiveData<String>()

    val totalViews: LiveData<String>
        get() = _totalViews
    private val _totalViews = MutableLiveData<String>()

    val avatarUrl: LiveData<Uri>
        get() = _avatarUrl
    private val _avatarUrl = MutableLiveData<Uri>()

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun start() {
        updateDataBindingObservables()
    }

    fun signIn() {
        _signInEvent.value = Event(Unit)
        updateDataBindingObservables()
    }

    fun signOut() {
        _signOutEvent.value = Event(Unit)
        updateDataBindingObservables()
    }

    fun userStatusChanged() {
        updateDataBindingObservables()
    }

    private fun updateDataBindingObservables() {
        val user = firebaseAuth.currentUser
        _totalPosts.value = "Total posts: *calculating*"
        _totalViews.value = "Total view: *calculating*"
        if (user == null) {
            _username.value = "anonymous"
            _signInButtonVisibility.value = View.VISIBLE
            _elementsVisibility.value = View.GONE
            _signOutButtonVisibility.value = View.GONE
        } else {
            _username.value = user.displayName
            _avatarUrl.value = user.photoUrl

            recipeRepository.getTotalPostsNumber(user.uid, object : RecipeDataSource.GetTotalPostsNumberCallback {
                override fun onTotalPostsNumberGot(totalPosts: Long) {
                    _totalPosts.value = "Total posts: $totalPosts"
                }

                override fun onDataNotAvailable() {
                    _totalPosts.value = "Total posts: 0"
                    Log.i("NYA", "Total posts data is not available (from VM)")
                }
            })

            recipeRepository.getTotalViewsCount(user.uid, object : RecipeDataSource.GetTotalViewsCountCallback {
                override fun onTotalViewCountGot(totalViews: Long) {
                    _totalViews.value = "Total views: $totalViews"
                }

                override fun onDataNotAvailable() {
                    _totalViews.value = "Total views: 0"
                    Log.i("NYA", "Total views data is not available (from VM)")
                }
            })

            _signInButtonVisibility.value = View.GONE
            _elementsVisibility.value = View.VISIBLE
            _signOutButtonVisibility.value = View.VISIBLE
        }
    }
}
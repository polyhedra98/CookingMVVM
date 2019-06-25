package com.mishenka.cookingmvvm.me

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.mishenka.cookingmvvm.R
import com.mishenka.cookingmvvm.databinding.MeFragmentBinding
import com.mishenka.cookingmvvm.util.Event
import com.mishenka.cookingmvvm.util.Utils.RC_SIGN_IN
import com.mishenka.cookingmvvm.util.obtainViewModel

class MeFragment : Fragment() {

    companion object {
        fun newInstance() = MeFragment()
    }

    private lateinit var viewDataBinding: MeFragmentBinding
    private lateinit var viewModel: MeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.me_fragment, container, false)
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel().apply {
            signInEvent.observe(this@MeFragment, Observer<Event<Unit>> { event->
                event.getContentIfNotHandled()?.let {
                    observeSignIn()
                }
            })
            signOutEvent.observe(this@MeFragment, Observer<Event<Unit>> { event ->
                event.getContentIfNotHandled()?.let {
                    observeSignOut()
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

    private fun observeSignIn() {
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
        )

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
        )
    }

    private fun observeSignOut() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            AuthUI.getInstance()
                    .signOut(this.activity!!)
                    .addOnCompleteListener {
                        viewDataBinding.viewModel?.userStatusChanged()
                    }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = IdpResponse.fromResultIntent(data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                viewDataBinding.viewModel?.userStatusChanged()
            } else {
                //TODO ("Sign in failed")
                Log.i("NYA", "Sign in failed. ${response?.error}")
            }
        }
    }

    private fun obtainViewModel(): MeViewModel = obtainViewModel(MeViewModel::class.java)
}
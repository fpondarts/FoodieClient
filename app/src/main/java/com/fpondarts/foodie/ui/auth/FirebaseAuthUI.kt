package com.fpondarts.foodie.ui.auth

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle

import com.firebase.ui.auth.AuthUI
import com.fpondarts.foodie.R
import com.google.firebase.auth.FirebaseAuth

import java.util.Arrays

class FirebaseAuthUI : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance() != null) {
            FirebaseAuth.getInstance().signOut()
        }
        AuthUI.getInstance().signOut(this@FirebaseAuthUI)
        setContentView(R.layout.activity_firebase_auth_ui)
        createSignInIntent()
    }


    fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = Arrays.asList(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
            RC_SIGN_IN
        )
        // [END auth_fui_create_intent]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(SIGN_UP_OK)
            } else {
                setResult(SIGN_UP_ERROR)
            }
            finish()
        }

    }

    companion object {


        private val RC_SIGN_IN = 123
        val SIGN_UP_OK = 0
        val SIGN_UP_ERROR = -1

    }

}

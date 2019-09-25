package com.fpondarts.foodie.ui.auth

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI

import com.fpondarts.foodie.R
import com.fpondarts.foodie.controller.MainActivity
import com.fpondarts.foodie.databinding.ActivitySignInBinding
import com.fpondarts.foodie.services.RetrofitClientInstance
import com.fpondarts.foodie.services.ServerAPI
import com.google.firebase.auth.FirebaseAuth
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class SignInActivity : AppCompatActivity(), AuthListener, KodeinAware {


    override val kodein by kodein()

    private val factory: SignInViewModelFactory by instance()

    private var mGoogleSignInButton: Button? = null

    private var mViewModel:SignInViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivitySignInBinding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in)
        val viewModel = ViewModelProviders.of(this,factory).get(SignInViewModel::class.java)
        binding.viewModel = viewModel
        mViewModel = viewModel
        viewModel.authListener = this

        viewModel.sessionToken.observe(this, androidx.lifecycle.Observer {
            if (! it.isNullOrBlank()){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        })

        viewModel.changeActivity.observe(this,androidx.lifecycle.Observer {
            if (it == viewModel.TO_SIGN_UP){
                startActivity(Intent(this,SignUpActivity::class.java))
            }
        })

    }

    private fun googleSignIn() {
        AuthUI.getInstance().signOut(this@SignInActivity)
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
            SignInActivity.RC_SIGN_IN
        )
        // [END auth_fui_create_intent]
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SignInActivity.RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                mViewModel!!.foodieSignIn(FirebaseAuth.getInstance().currentUser!!)
            } else {
                Toast.makeText(this@SignInActivity, "Hubo un error en el ingreso", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onStarted() {

    }
    override fun onSuccess() {

    }

    override fun onFailure(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }


    companion object {
        private val RC_SIGN_IN = 123
    }


}

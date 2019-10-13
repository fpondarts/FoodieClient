package com.fpondarts.foodie.ui.auth

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.firebase.ui.auth.AuthUI
import com.fpondarts.foodie.R
import com.fpondarts.foodie.controller.UserRegisterFirstInput
import com.fpondarts.foodie.databinding.ActivitySignUpBinding
import com.google.android.gms.common.SignInButton
import org.kodein.di.KodeinAware

import java.util.Arrays

import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : AppCompatActivity(),AuthListener, KodeinAware{

    override val kodein by kodein()

    private val factory: FoodieViewModelFactory by instance()

    private var mGoogleSignInButton: SignInButton? = null

    private var mViewModel:SignUpViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivitySignUpBinding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        val viewModel = ViewModelProviders.of(this,factory).get(SignUpViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.authListener = this
        mViewModel = viewModel
        mGoogleSignInButton = findViewById(R.id.googleSignUpButton)
        mGoogleSignInButton?.let {
            it.setOnClickListener { googleSignIn() }
        }

        val changeActivityObserver = Observer<Int>{
            if (it==-1){
                val intent = Intent(this,SignInActivity::class.java)
                startActivity(intent)
            } else {
                if (it==1){
                    val intent = Intent(this,UserRegisterFirstInput::class.java)
                    intent.putExtra("name", viewModel.fullName)
                    intent.putExtra("email", viewModel.email)
                    intent.putExtra("password", viewModel.password)
                    intent.putExtra("photo", viewModel.photoUri)
                    intent.putExtra("uid", viewModel.uid)
                    startActivity(intent)
                }
            }
        }

        viewModel.changeActivity.observe(this,changeActivityObserver)

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                mViewModel!!.onGoogleSignIn()
            } else {
                Toast.makeText(this@SignUpActivity, "Hubo un error en el ingreso", Toast.LENGTH_LONG).show()
            }
        }

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


    private fun googleSignIn() {
        AuthUI.getInstance().signOut(this@SignUpActivity)
        createSignInIntent()
    }

    companion object {
        private val RC_SIGN_IN = 123
    }

    override fun onStarted() {

    }
    override fun onSuccess() {

    }

    override fun onFailure(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }





}

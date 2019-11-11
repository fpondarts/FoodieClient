package com.fpondarts.foodie.ui.auth

import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel(
    private val repository: Repository
): ViewModel()

{
    var fullName: String? = null
    var email: String? = null
    var password: String? = null
    var uid: String? = null
    var photoUri: String? = null
    var isGoogle: Boolean = false

    var authListener: AuthListener? = null

    val SIGN_IN = -1
    val STAY = 0
    val SIGN_UP_INPUT = 1;

    val changeActivity = MutableLiveData<Int>(STAY)

    fun isEmailValid(email: String?): Boolean {
        if (email.isNullOrBlank()){
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    fun onSignUpButtonClick(view: View,fullName:String,email:String,password:String){
        if (fullName.isNullOrBlank() || email.isNullOrBlank() || password.isNullOrBlank()){
            authListener?.onFailure("Invalid user data")
            authListener?.onFailure("fullName: "+fullName)
            authListener?.onFailure("email: "+email)
            authListener?.onFailure("password: "+password)

            return
        }

        val auth = FirebaseAuth.getInstance()
        auth?.createUserWithEmailAndPassword(email!!,password!!)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    this.fullName = fullName
                    this.email = email
                    this.password = password
                    uid = task.result!!.user!!.uid
                    changeActivity.value = SIGN_UP_INPUT
                } else {
                    authListener?.onFailure(task.exception!!.message!!)
                }
            }

    }


    fun onSignInButtonClick(view: View){
        changeActivity.value = SIGN_IN
        return
    }

    fun onGoogleSignIn(){
        val user = FirebaseAuth.getInstance().currentUser
        isGoogle = true
        fullName = user!!.displayName
        email = user!!.email
        uid = user!!.uid
        photoUri = user!!.photoUrl.toString()
        changeActivity.value = SIGN_UP_INPUT
    }

}
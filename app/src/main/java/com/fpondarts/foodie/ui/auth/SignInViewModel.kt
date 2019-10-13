package com.fpondarts.foodie.ui.auth

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult

class SignInViewModel(
    private val repository: Repository
) : ViewModel(){

    val STAY = 0
    val TO_SIGN_UP = 1
    val changeActivity = MutableLiveData<Int>(STAY)

    var email:String? = null
    var password:String? = null
    var authListener:AuthListener? = null

    var sessionToken = MutableLiveData<String>(null)

    var auth = FirebaseAuth.getInstance()



    fun onSignUpButtonClick(view: View){
        changeActivity.value = TO_SIGN_UP
    }

    fun isEmailValid(email: String?): Boolean {
        if (email.isNullOrBlank()){
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    fun onSignInButtonClick(view: View){

        if (email.isNullOrBlank() || password.isNullOrBlank() || !isEmailValid(email)){
            authListener?.onFailure("Wrong user data")
            return
        }

        auth = FirebaseAuth.getInstance()
        auth?.signInWithEmailAndPassword(email!!,password!!)
        .addOnCompleteListener{ task : Task<AuthResult> ->
            if (task.isSuccessful) {
                foodieSignIn(task.getResult()!!.user!!)
            } else {
                authListener?.onFailure("Authentication failed")
            }
        }

    }



    fun foodieSignIn(user:FirebaseUser){
        auth.currentUser!!.getIdToken(true).addOnCompleteListener{task: Task<GetTokenResult> ->
            if (task.isSuccessful){
                Coroutines.main {
                    try{
                        val signInResponse = repository.foodieSignIn(user.email!!,password!!,task.getResult()!!.token!!)
                        this.sessionToken.value = signInResponse.sessionToken
                    }catch (e:FoodieApiException){
                        authListener?.onFailure("Foodie Sign In failure")
                    }

                }
            } else {
                authListener?.onFailure("Authentication failed (Couldn't get idToken)")
            }
        }
    }
}
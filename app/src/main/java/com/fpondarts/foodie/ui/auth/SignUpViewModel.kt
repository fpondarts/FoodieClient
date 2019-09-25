package com.fpondarts.foodie.ui.auth

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel(
    private val repository: UserRepository
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

    fun onSignUpButtonClick(view: View){
        if (fullName.isNullOrBlank() || email.isNullOrBlank() || password.isNullOrBlank() || !isEmailValid(email)){
            authListener?.onFailure("Invalid user data")
            return
        }

        Coroutines.main{
            try{
                val availabilityResponse = repository.checkAvailability(email!!)
                availabilityResponse.isAvailable?.let{
                    if (!it){
                        authListener?.onFailure("Email not available")
                    } else {
                        changeActivity.value = SIGN_UP_INPUT;
                    }
                    return@main
                }
                authListener?.onFailure(availabilityResponse?.message!!)
            } catch (e:FoodieApiException){
                authListener?.onFailure(e.message!!)
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
package com.fpondarts.foodie.ui.auth2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fpondarts.foodie.data.repository.AuthRepository
import com.fpondarts.foodie.util.ApiExceptionHandler
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AuthActivity : AppCompatActivity(), KodeinAware, ApiExceptionHandler {



    override val kodein by kodein()

    val repository : AuthRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository.apiErrorHandler = this
    }

    override fun handle(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}

package com.fpondarts.foodie.ui.auth2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.AuthRepository
import com.fpondarts.foodie.util.ApiExceptionHandler
import com.google.firebase.auth.FirebaseAuth
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AuthActivity : AppCompatActivity(), KodeinAware{



    override val kodein by kodein()

    val repository : AuthRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val navController = findNavController(R.id.nav_host_fragment)

        repository.apiErrors.observe(this, Observer {
            it?.let{
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
        })

    }
}

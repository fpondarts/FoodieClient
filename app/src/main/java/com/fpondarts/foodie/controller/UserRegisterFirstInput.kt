package com.fpondarts.foodie.controller

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import com.fpondarts.foodie.R
import com.fpondarts.foodie.model.FoodieUser
import com.fpondarts.foodie.services.RetrofitClientInstance
import com.fpondarts.foodie.services.ServerAPI
import com.fpondarts.foodie.ui.auth.SignInActivity

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRegisterFirstInput : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var mTvWelcome: TextView? = null
    private val mEtPhone: EditText? = null
    private val mEtCredCard: EditText? = null
    private val mEtCvv: EditText? = null
    private var mImPhoto: ImageView? = null
    private var mSpRoles: Spinner? = null

    private var mSignUp: Button? = null
    private var mCancel: Button? = null
    private var mUser: FoodieUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register_first_input)
        val intent = intent
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val photoUri = intent.getStringExtra("photo")

        mTvWelcome = findViewById<View>(R.id.textViewWelcome) as TextView
        mTvWelcome!!.text = "Hola, " + name!!.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]

        mUser = FoodieUser(name, email)

        mCancel = findViewById<View>(R.id.buttonCancelSignUp) as Button
        mCancel!!.setOnClickListener {
            val intent = Intent(this@UserRegisterFirstInput, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }


        mSpRoles = findViewById<View>(R.id.spinnerRoles) as Spinner

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ROLES)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpRoles!!.adapter = adapter
        mSpRoles!!.setSelection(0)
        mSignUp = findViewById<View>(R.id.buttonFinishSignUp) as Button
        mSignUp!!.setOnClickListener {
            val call = service.signUpUser(FoodieUser(name, email))

            call.enqueue(object : Callback<FoodieUser> {
                override fun onResponse(call: Call<FoodieUser>, response: Response<FoodieUser>) {

                    if (response.code() == 200) {
                        val intent = Intent(this@UserRegisterFirstInput, UserDataActivity::class.java)
                        val user = response.body()
                        intent.putExtra("fullName", user!!.fullName)
                        intent.putExtra("email", user.email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@UserRegisterFirstInput, "No se ha podido registrar", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<FoodieUser>, t: Throwable) {
                    Toast.makeText(this@UserRegisterFirstInput, "Problemas de conexion con el servidor", Toast.LENGTH_LONG).show()
                }
            })
        }


        mImPhoto = findViewById<View>(R.id.imageViewPhoto) as ImageView
        if (photoUri != null) {
            mImPhoto!!.setImageURI(Uri.parse(photoUri))
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View,
                                pos: Int, id: Long) {


    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    companion object {

        internal var service = RetrofitClientInstance.retrofitInstance.create(ServerAPI::class.java!!)


        private val ROLES = arrayOf("User", "Delivery")
    }
}

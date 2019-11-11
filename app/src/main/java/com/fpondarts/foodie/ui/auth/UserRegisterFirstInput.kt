package com.fpondarts.foodie.ui.auth

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView

import com.fpondarts.foodie.R
import com.fpondarts.foodie.model.FoodieUser
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.network.request.UserRegisterRequest
import com.fpondarts.foodie.services.RetrofitClientInstance
import com.fpondarts.foodie.services.ServerAPI
import com.fpondarts.foodie.util.Coroutines
import com.squareup.picasso.Picasso
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class UserRegisterFirstInput : AppCompatActivity(), KodeinAware{

    private var mTvWelcome: TextView? = null
    private var mEtPhone: EditText? = null
    private val mEtCredCard: EditText? = null
    private val mEtCvv: EditText? = null
    private var mImPhoto: ImageView? = null
    private var mSpRoles: Spinner? = null

    private var mSignUp: Button? = null
    private var mCancel: Button? = null
    private var mUser: FoodieUser? = null

    private var name:String? = null
    private var email:String? = null
    private var photoUri:String? = null
    private var password:String? = null
    private var uid:String? = null


    override val kodein by kodein()

    private val api:FoodieApi by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register_first_input)
        val intent = intent
        name = intent.getStringExtra("name")
        email = intent.getStringExtra("email")
        photoUri = intent.getStringExtra("photo")
        password = intent.getStringExtra("password")
        uid = intent.getStringExtra("uid")


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
        mEtPhone = findViewById<View>(R.id.etPhone) as EditText
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ROLES
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpRoles!!.adapter = adapter
        mSpRoles!!.setSelection(0)
        mSignUp = findViewById<View>(R.id.buttonFinishSignUp) as Button
        mSignUp!!.setOnClickListener {


        }


        mImPhoto = findViewById<View>(R.id.imageViewPhoto) as ImageView
        if (photoUri != null) {
            Picasso.get().load(photoUri).into(mImPhoto)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()


    }


    companion object {

        internal var service = RetrofitClientInstance.retrofitInstance.create(ServerAPI::class.java!!)

        private val ROLES = arrayOf("User", "Delivery")
    }
}

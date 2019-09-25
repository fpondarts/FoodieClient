package com.fpondarts.foodie.controller

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.fpondarts.foodie.R
import com.fpondarts.foodie.ui.auth.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class UserDataActivity : AppCompatActivity() {

    private var mTvFullname: TextView? = null
    private var mTvEmail: TextView? = null
    //private TextView mTvRole;


    private var mSignOutButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)

        val intent = intent
        mTvFullname = findViewById<View>(R.id.tvFullName) as TextView
        mTvEmail = findViewById<View>(R.id.tvEmail) as TextView
        //mTvRole = (TextView) findViewById(R.id.tvRole);
        mTvFullname!!.text = intent.getStringExtra("fullName")
        mTvEmail!!.text = intent.getStringExtra("email")
        //mTvRole.setText(intent.getStringExtra("role"));


        mSignOutButton = findViewById<View>(R.id.signOutButton) as Button
        mSignOutButton!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val signIn = Intent(this@UserDataActivity, SignInActivity::class.java)
            startActivity(signIn)
            finish()
        }
    }
}

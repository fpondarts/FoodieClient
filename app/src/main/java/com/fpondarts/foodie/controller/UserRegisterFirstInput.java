package com.fpondarts.foodie.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.fpondarts.foodie.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserRegisterFirstInput extends AppCompatActivity {


    private EditText mName;
    private EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_first_input);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        Uri photoUrl = Uri.parse(intent.getStringExtra("photoUrl"));

    }
}

package com.fpondarts.foodie.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fpondarts.foodie.R;

public class SignUpActivity extends AppCompatActivity {

    private Button mSignUpButton;
    private Button mToSignInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mSignUpButton = findViewById(R.id.signUpButton);
        mSignUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(SignUpActivity.this,"SignUp",Toast.LENGTH_LONG).show();
            }
        });
        mToSignInButton = findViewById(R.id.toSignInButton);
        mToSignInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}

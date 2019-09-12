package com.fpondarts.foodie.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpondarts.foodie.R;
import com.fpondarts.foodie.services.RetrofitClientInstance;
import com.fpondarts.foodie.services.ServerAPI;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private Button mSignInButton;
    private Button mSignUpButton;
    private EditText mUsername;
    private EditText mPassword;

    static ServerAPI service = RetrofitClientInstance.getRetrofitInstance().create(ServerAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            auth.signOut();
            Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else {

            mUsername = (EditText) findViewById(R.id.userNameInput);
            mPassword = (EditText) findViewById(R.id.passwordInput);
            mSignInButton = (Button) findViewById(R.id.signInButton);

            mSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUsername.toString().isEmpty() || mPassword.toString().isEmpty()){
                        Toast.makeText(SignInActivity.this,"Ingrese usuario y contraseña",Toast.LENGTH_LONG).show();
                    }

                    Toast.makeText(SignInActivity.this,"Sign In",Toast.LENGTH_SHORT).show();
                }
            });

            mSignUpButton = (Button) findViewById(R.id.signUpButton);
            mSignUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });

        }
    }
}

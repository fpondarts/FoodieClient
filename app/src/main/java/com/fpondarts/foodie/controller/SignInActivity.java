package com.fpondarts.foodie.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpondarts.foodie.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private Button mSignInButton;
    private Button mSignUpButton;
    private Button mToFirebaseButton;
    private EditText mUsername;
    private EditText mPassword;

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
                    if (mUsername.toString().isEmpty()){

                    }
                    Toast.makeText(SignInActivity.this, "Inicio sesion",Toast.LENGTH_LONG).show();
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

            mToFirebaseButton = (Button) findViewById(R.id.toFirebase);
            mToFirebaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(SignInActivity.this,FirebaseAuthUI.class);
                    startActivity(intent);
                }
            });



        }
    }
}

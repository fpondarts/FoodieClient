package com.fpondarts.foodie.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.fpondarts.foodie.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class FirebaseAuthUI extends AppCompatActivity {


    private static final int RC_SIGN_IN = 123;
    public static final int SIGN_UP_OK = 0;
    public static final int SIGN_UP_ERROR = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance()!=null){
            FirebaseAuth.getInstance().signOut();
        }
        AuthUI.getInstance().signOut(FirebaseAuthUI.this);
        setContentView(R.layout.activity_firebase_auth_ui);
        createSignInIntent();
    }



    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
                );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                setResult(SIGN_UP_OK);
            } else {
                setResult(SIGN_UP_ERROR);
            }
            finish();
        }

    }

}

package com.fpondarts.foodie.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fpondarts.foodie.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDataActivity extends AppCompatActivity {

    private TextView userData;
    private Button mSignOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String text = user.getDisplayName()+"\n"+user.getEmail();
        userData = (TextView) findViewById(R.id.userDataText);
        userData.setText(text);

        mSignOutButton = (Button) findViewById(R.id.signOutButton);
        mSignOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Intent signIn = new Intent(UserDataActivity.this,SignInActivity.class);
                startActivity(signIn);
                finish();
            }
        });
    }
}

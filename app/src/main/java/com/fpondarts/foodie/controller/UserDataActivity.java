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

    private TextView mTvFullname;
    private TextView mTvEmail;
    //private TextView mTvRole;



    private Button mSignOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        Intent intent = getIntent();
        mTvFullname = (TextView) findViewById(R.id.tvFullName);
        mTvEmail = (TextView) findViewById(R.id.tvEmail);
        //mTvRole = (TextView) findViewById(R.id.tvRole);
        mTvFullname.setText(intent.getStringExtra("fullName"));
        mTvEmail.setText(intent.getStringExtra("email"));
        //mTvRole.setText(intent.getStringExtra("role"));


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

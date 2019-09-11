package com.fpondarts.foodie.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fpondarts.foodie.R;
import com.fpondarts.foodie.model.FoodieUser;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

public class UserRegisterFirstInput extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final String[] ROLES = {"User","Delivery"};

    private  TextView mTvWelcome;
    private EditText mEtPhone;
    private EditText mEtCredCard;
    private EditText mEtCvv;
    private ImageView mImPhoto;
    private Spinner mSpRoles;

    private FoodieUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_first_input);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String photoUri = intent.getStringExtra("photo");

        mTvWelcome = (TextView) findViewById(R.id.textViewWelcome);
        mTvWelcome.setText("Hola, ".concat(name.split(" ")[0]));

        mUser = new FoodieUser(name,email);


        mSpRoles = (Spinner) findViewById(R.id.spinnerRoles);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ROLES);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpRoles.setAdapter(adapter);


        mImPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        if (photoUri!=null){
            mImPhoto.setImageURI(Uri.parse(photoUri));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}

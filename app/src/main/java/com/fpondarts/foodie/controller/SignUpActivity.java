package com.fpondarts.foodie.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpondarts.foodie.R;
import com.fpondarts.foodie.services.ServerAPI;
import com.fpondarts.foodie.services.RetrofitClientInstance;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_UP = 1;

    private Button mSignUpButton;
    private SignInButton mGoogleSignInButton;
    private Button mToSignInButton;

    private EditText mEtFullName;
    private EditText mEtEmail;
    private EditText mEtPassword;


    static ServerAPI service = RetrofitClientInstance.getRetrofitInstance().create(ServerAPI.class);



    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mEtFullName = (EditText) findViewById(R.id.etName);
        mEtEmail = (EditText) findViewById(R.id.etEmail);
        mEtPassword = (EditText) findViewById(R.id.etPassword);

        mSignUpButton = (Button) findViewById(R.id.signUpButton);
        mSignUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (mEtFullName.getText().toString().isEmpty() || mEtEmail.getText().toString().isEmpty() ||
                    mEtPassword.getText().toString().isEmpty()){

                    Toast.makeText(SignUpActivity.this,"Los campos son obligatorios",Toast.LENGTH_LONG).show();

                } else {
                    toRegisterInput(mEtFullName.getText().toString(),
                            mEtEmail.getText().toString(),
                            mEtPassword.getText().toString(),
                            null, null);
                }
            }
        });

        mGoogleSignInButton = findViewById(R.id.googleSignInButton);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
        mToSignInButton = (Button) findViewById(R.id.toSignInButton);
        mToSignInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == GOOGLE_SIGN_UP){
            if(resultCode==FirebaseAuthUI.SIGN_UP_OK){

                FirebaseUser account = FirebaseAuth.getInstance().getCurrentUser();
                final String name = account.getDisplayName();
                final String email = account.getEmail();
                final Uri photoUrl = account.getPhotoUrl();
                final String uid = account.getUid();

                toRegisterInput(name,email,null,photoUrl.toString(),uid);

            } else {
               Toast.makeText(SignUpActivity.this,"Hubo un error en el ingreso",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void toRegisterInput(final String name,final String email,final String password,final String photo,final String uid){

        Call<Void> call = service.checkEmailIsAvailable(email);

        call.enqueue(new Callback<Void>() {
             @Override
             public void onResponse(Call<Void> call, Response<Void> response) {

                 if (response.code() == 404) {
                     Intent intent = new Intent(SignUpActivity.this,UserRegisterFirstInput.class);
                     intent.putExtra("name",name);
                     intent.putExtra("email",email);
                     intent.putExtra("password",password);
                     intent.putExtra("photo",photo);
                     intent.putExtra("uid",uid);
                     startActivity(intent);
                     finish();
                 } else {
                     Toast.makeText(SignUpActivity.this, "Ya existe un usuario asociado a esa cuenta\nInicie sesión.", Toast.LENGTH_LONG).show();
                 }
             }

            @Override
            public void onFailure(Call<Void> call, Throwable t){
                Toast.makeText(SignUpActivity.this,"Problemas de conexion con el servidor",Toast.LENGTH_LONG).show();
            }
         });
    }


    private void googleSignIn() {
        Intent intent = new Intent(SignUpActivity.this,FirebaseAuthUI.class);
        startActivityForResult(intent,GOOGLE_SIGN_UP);
    }



}

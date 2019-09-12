package com.fpondarts.foodie.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fpondarts.foodie.R;
import com.fpondarts.foodie.model.FoodieUser;
import com.fpondarts.foodie.services.RetrofitClientInstance;
import com.fpondarts.foodie.services.ServerAPI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRegisterFirstInput extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static ServerAPI service = RetrofitClientInstance.getRetrofitInstance().create(ServerAPI.class);


    private static final String[] ROLES = {"User","Delivery"};

    private  TextView mTvWelcome;
    private EditText mEtPhone;
    private EditText mEtCredCard;
    private EditText mEtCvv;
    private ImageView mImPhoto;
    private Spinner mSpRoles;

    private Button mSignUp;
    private Button mCancel;
    private FoodieUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_first_input);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String email = intent.getStringExtra("email");
        String photoUri = intent.getStringExtra("photo");

        mTvWelcome = (TextView) findViewById(R.id.textViewWelcome);
        mTvWelcome.setText("Hola, ".concat(name.split(" ")[0]));

        mUser = new FoodieUser(name,email);

        mCancel = (Button) findViewById(R.id.buttonCancelSignUp);
        mCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserRegisterFirstInput.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mSpRoles = (Spinner) findViewById(R.id.spinnerRoles);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ROLES);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpRoles.setAdapter(adapter);
        mSpRoles.setSelection(0);
        mSignUp = (Button) findViewById(R.id.buttonFinishSignUp);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<FoodieUser> call = service.signUpUser(new FoodieUser(name,email));

                call.enqueue(new Callback<FoodieUser>() {
                    @Override
                    public void onResponse(Call<FoodieUser> call, Response<FoodieUser> response) {

                        if (response.code() == 200) {
                            Intent intent = new Intent(UserRegisterFirstInput.this, UserDataActivity.class);
                            FoodieUser user = response.body();
                            intent.putExtra("fullName",user.getFullName());
                            intent.putExtra("email",user.getEmail());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UserRegisterFirstInput.this, "No se ha podido registrar", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FoodieUser> call, Throwable t) {
                        Toast.makeText(UserRegisterFirstInput.this, "Problemas de conexion con el servidor", Toast.LENGTH_LONG).show();
                    }
                });

            }

        });


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

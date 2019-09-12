package com.fpondarts.foodie.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.io.StringReader;

public class FoodieUser implements Serializable {


    private String email;
    private String fullName;


    public FoodieUser(String name, String email){
        this.email = email;
        this.fullName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}

package com.fpondarts.foodie.model;

import android.graphics.Bitmap;

public class FoodieUser {
    private String mEmail;
    private String mName ;
    private Bitmap mPicture;
    private String mPhone;

    public FoodieUser(String email, String name){
        mEmail = email;
        mName = name;
    }

    public String getmEmail() {
        return mEmail;
    }


    public String getmName() {
        return mName;
    }

    public Bitmap getmPicture() {
        return mPicture;
    }

    public void setmPicture(Bitmap mPicture) {
        this.mPicture = mPicture;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }
}

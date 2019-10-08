package com.fpondarts.foodie.ui.home.ui.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileViewModel (repository: Repository): ViewModel() {

    var name:String?=null
    var email:String?=null

}
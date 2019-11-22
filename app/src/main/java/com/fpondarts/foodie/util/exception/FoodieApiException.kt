package com.fpondarts.foodie.util.exception

import java.io.IOException

class FoodieApiException(message:String,val code: Int = 400): IOException(message){

}

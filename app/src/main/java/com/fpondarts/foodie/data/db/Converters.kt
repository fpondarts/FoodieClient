package com.fpondarts.foodie.data.db

import androidx.room.TypeConverter
import com.fpondarts.foodie.model.OfferState

class Converters {

    companion object{
        @TypeConverter
        @JvmStatic
        fun fromStringToOfferState(value:String):OfferState{

            if (value=="waiting"){
                return OfferState.WAITING
            } else if (value=="accepted"){
                return OfferState.ACCEPTED
            }

            return OfferState.REJECTED
        }
    }

}
package com.fpondarts.foodie.data.db

import androidx.room.TypeConverter
import com.fpondarts.foodie.data.db.entity.Offer
import com.fpondarts.foodie.model.OfferState
import com.fpondarts.foodie.model.OrderState
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun fromStringToOfferState(value:String):OfferState{
        if (value=="waiting"){
            return OfferState.WAITING
        } else if (value=="accepted"){
            return OfferState.ACCEPTED
        }
        return OfferState.REJECTED
    }

    @TypeConverter
    fun fromOfferStateToString(value:OfferState):String{
        if (value == OfferState.WAITING){
            return "waiting"
        } else if (value == OfferState.ACCEPTED){
            return "accepted"
        }
        return "rejected"
    }

    @TypeConverter
    fun orderStateToString(value:OrderState):String{
        return value.stringVal
    }

    @TypeConverter
    fun stringToOrderState(value:String):OrderState{
        OrderState.values().forEach {
            if (it.stringVal == value)
                return it
        }
        return OrderState.CANCELLED
    }


}
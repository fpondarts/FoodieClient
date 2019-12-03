package com.fpondarts.foodie

import android.app.Application
import com.fpondarts.foodie.data.db.FoodieDatabase
import com.fpondarts.foodie.data.repository.AuthRepository
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.network.DirectionsApi
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import com.fpondarts.foodie.ui.auth2.AuthViewModelFactory
import com.fpondarts.foodie.ui.delivery.DeliveryViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class FoodieApp: Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@FoodieApp))
        bind() from singleton { FoodieApi() }
        bind() from singleton { AuthRepository(instance())}
        bind() from singleton { FoodieDatabase(instance()) }
        bind() from singleton { UserRepository(instance(),instance(),instance()) }
        bind() from singleton { DeliveryRepository(instance() , instance(), instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { FoodieViewModelFactory(instance()) }
        bind() from provider { DeliveryViewModelFactory (instance()) }
        bind() from singleton { DirectionsApi() }
    }
}
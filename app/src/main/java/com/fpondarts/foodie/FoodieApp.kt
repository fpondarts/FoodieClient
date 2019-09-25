package com.fpondarts.foodie

import android.app.Application
import com.fpondarts.foodie.data.db.FoodieDatabase
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.ui.auth.SignInViewModel
import com.fpondarts.foodie.ui.auth.SignInViewModelFactory
import com.fpondarts.foodie.ui.auth.SignUpViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class FoodieApp: Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@FoodieApp))
        bind() from singleton { FoodieApi() }
        bind() from singleton { FoodieDatabase(instance()) }
        bind() from singleton { UserRepository(instance(),instance()) }
        bind() from provider { SignUpViewModelFactory(instance()) }
        bind() from provider { SignInViewModelFactory(instance())}


    }
}
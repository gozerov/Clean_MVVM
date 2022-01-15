package com.example.clean_mvvm.application

import android.app.Application
import android.content.Context
import com.example.clean_mvvm.dagger.AppComponent
import com.example.clean_mvvm.dagger.DaggerAppComponent


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        appComponent = DaggerAppComponent
            .builder()
            .context(applicationContext)
            .build()
        appComponent.inject(this)
        super.onCreate()
    }

}
val Context.appComponent: AppComponent
    get() = when(this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }


const val APP_PREFERENCES = "APP_SHARED_PREFERENCES"
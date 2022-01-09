package com.example.clean_mvvm.application

import android.app.Application
import foundation.dagger.AppComponent
import foundation.dagger.DaggerAppComponent

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
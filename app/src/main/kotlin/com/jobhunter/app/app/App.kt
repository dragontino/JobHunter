package com.jobhunter.app.app

import android.app.Application
import com.jobhunter.app.di.AppComponent
import com.jobhunter.app.di.DaggerAppComponent
import com.jobhunter.app.di.DataModule

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .dataModule(DataModule(context = this))
            .build()
    }
}
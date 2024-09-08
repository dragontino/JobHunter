package com.jobhunter.app.app

import android.app.Application
import com.jobhunter.app.di.AppComponent
import com.jobhunter.app.di.DaggerAppComponent

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.create()
    }
}
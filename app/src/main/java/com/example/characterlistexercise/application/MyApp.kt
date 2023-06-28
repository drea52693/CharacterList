package com.example.characterlistexercise.application

import android.app.Application
import com.example.characterlistexercise.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(viewModelModule)
        }
    }
}
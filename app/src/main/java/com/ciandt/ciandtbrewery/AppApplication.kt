package com.ciandt.ciandtbrewery

import android.app.Application
import com.ciandt.ciandtbrewery.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(
                listOf(
                    databaseModule,
                    daoModule,
                    webClientModule,
                    repositoryModule,
                    viewModelModule,
                    retrofitModule,
                    serviceModule
                )
            )
        }
    }
}
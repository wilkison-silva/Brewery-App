package com.ciandt.ciandtbrewery

import android.app.Application
import com.ciandt.ciandtbrewery.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestApplication)
            allowOverride(override = true)
            modules(
                databaseModule,
                dataModuleTest,
                daoModule,
                serviceModule,
                viewModelModule,
                repositoryModule,
                retrofitModule,
                webClientModule
            )
        }
    }
}
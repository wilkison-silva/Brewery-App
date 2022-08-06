package com.ciandt.ciandtbrewery

import androidx.room.Room
import com.ciandt.ciandtbrewery.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModuleTest = module {
    single { Room
        .inMemoryDatabaseBuilder(androidContext(),
            AppDatabase::class.java
        ).build()
    }
}
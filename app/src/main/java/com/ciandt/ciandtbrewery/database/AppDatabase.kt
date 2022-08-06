package com.ciandt.ciandtbrewery.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ciandt.ciandtbrewery.database.dao.BreweryDao
import com.ciandt.ciandtbrewery.database.model.BreweryData

@Database(entities = [BreweryData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun breweryDao(): BreweryDao

    companion object{

        const val DATABASE_NAME = "brewery_db"
    }
}
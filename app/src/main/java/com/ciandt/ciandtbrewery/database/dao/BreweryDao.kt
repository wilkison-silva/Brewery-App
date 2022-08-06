package com.ciandt.ciandtbrewery.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ciandt.ciandtbrewery.database.model.BreweryData

@Dao
interface BreweryDao {

    @Query("SELECT * FROM BreweryData")
    suspend fun getAll(): List<BreweryData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(breweryEntity: BreweryData)

    @Query("DELETE FROM breweryData WHERE id = :id")
    suspend fun deleteById(id: String)

}
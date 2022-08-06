package com.ciandt.ciandtbrewery.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BreweryData(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val average: Double,
    val breweryType: String?,
)

package com.test.devstree_test.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val isPrimary: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)

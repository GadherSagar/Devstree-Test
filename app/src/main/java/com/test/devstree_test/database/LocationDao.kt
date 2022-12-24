package com.test.devstree_test.database

import androidx.room.*
import com.test.devstree_test.database.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(locationEntity: LocationEntity): Long

    @Delete
    suspend fun deleteLocation(locationEntity: LocationEntity): Int

    @Update
    suspend fun updateLocation(locationEntity: LocationEntity): Int

    @Query("SELECT * FROM locationentity")
    fun getLocations(): Flow<List<LocationEntity>>
}
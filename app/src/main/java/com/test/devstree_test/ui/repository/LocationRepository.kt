package com.test.devstree_test.ui.repository

import com.test.devstree_test.App.Companion.appContext
import com.test.devstree_test.BuildConfig.MAPS_API_KEY
import com.test.devstree_test.database.AppDatabase
import com.test.devstree_test.database.entity.LocationEntity
import com.test.devstree_test.network.ApiService
import com.test.devstree_test.network.RetrofitClient
import com.test.devstree_test.network.dto.response.directions.Directions
import kotlinx.coroutines.flow.Flow

class LocationRepository {
    private val db = AppDatabase.getDatabase(appContext)
    private val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

    suspend fun insertLocation(locationEntity: LocationEntity) {
        db.locationDao.insertLocation(locationEntity)
    }

    suspend fun updateLocation(locationEntity: LocationEntity) {
        db.locationDao.updateLocation(locationEntity)
    }

    suspend fun deleteLocation(locationEntity: LocationEntity) {
        db.locationDao.deleteLocation(locationEntity)
    }

    fun getLocations(): Flow<List<LocationEntity>> {
        return db.locationDao.getLocations()
    }

    suspend fun getDirection(
        origin: LocationEntity, destination: LocationEntity, vararg waypoints: LocationEntity
    ): Directions? {
        var wayPointString = ""
        waypoints.forEach {
            wayPointString += "${it.name}|"
        }
        if (wayPointString.endsWith("|")) {
            wayPointString = wayPointString.substring(0, wayPointString.length - 1)
        }
        return try {
            val directionRes = apiService.getDirection(
                origin.name, destination.name, wayPointString, MAPS_API_KEY
            )
            directionRes
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

}
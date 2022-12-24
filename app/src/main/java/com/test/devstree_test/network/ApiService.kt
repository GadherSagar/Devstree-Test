package com.test.devstree_test.network

import com.test.devstree_test.network.Constant.DIRECTION_API
import com.test.devstree_test.network.dto.response.directions.Directions
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(DIRECTION_API)
    suspend fun getDirection(
        @Query(value = "origin", encoded = true) origin: String,
        @Query(value = "destination", encoded = true) destination: String,
        @Query(value = "waypoints", encoded = true) waypoints: String? = null,
        @Query(value = "key", encoded = true) key: String
    ):Directions
}
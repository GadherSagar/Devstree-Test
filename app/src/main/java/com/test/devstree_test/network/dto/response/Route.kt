package com.test.devstree_test.network.dto.response

data class Route(
    val startName: String = "",
    val endName: String = "",
    val startLat: Double?,
    val startLng: Double?,
    val endLat: Double?,
    val endLng: Double?,
    val overviewPolyline: String = ""
)

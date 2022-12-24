package com.test.devstree_test.network.dto.response.directions

import com.google.gson.annotations.SerializedName

data class EndLocation(@SerializedName("lng")
                       val lng: Double = 0.0,
                       @SerializedName("lat")
                       val lat: Double = 0.0)
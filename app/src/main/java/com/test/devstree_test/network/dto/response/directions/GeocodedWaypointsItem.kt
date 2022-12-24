package com.test.devstree_test.network.dto.response.directions

import com.google.gson.annotations.SerializedName

data class GeocodedWaypointsItem(@SerializedName("types")
                                 val types: List<String>?,
                                 @SerializedName("geocoder_status")
                                 val geocoderStatus: String = "",
                                 @SerializedName("place_id")
                                 val placeId: String = "")
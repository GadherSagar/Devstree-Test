package com.test.devstree_test.network.dto.response.directions

import com.google.gson.annotations.SerializedName

data class Polyline(@SerializedName("points")
                    val points: String = "")
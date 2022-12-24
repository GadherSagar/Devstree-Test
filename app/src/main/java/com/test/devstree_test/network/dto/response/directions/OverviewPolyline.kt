package com.test.devstree_test.network.dto.response.directions

import com.google.gson.annotations.SerializedName

data class OverviewPolyline(@SerializedName("points")
                            val points: String = "")
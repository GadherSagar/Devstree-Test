package com.test.devstree_test.network.dto.response.directions

import com.google.gson.annotations.SerializedName

data class Bounds(@SerializedName("southwest")
                  val southwest: Southwest,
                  @SerializedName("northeast")
                  val northeast: Northeast
)
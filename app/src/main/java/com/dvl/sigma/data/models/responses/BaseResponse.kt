package com.dvl.sigma.data.models.responses

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("msg")
    val message: String? = null,
    @SerializedName("status")
    val status: String = "0",
    @SerializedName("data")
    val data: T? = null,
    val isSuccessful: Boolean = status == "1"
)

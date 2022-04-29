package com.dvl.sigma.data.models.responses

import com.dvl.sigma.data.models.data.CategoryData
import com.google.gson.annotations.SerializedName

data class CategoryResponse(

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("msg")
    val message: String? = null,

    @SerializedName("data")
    val categoryData: CategoryData? = null,

    val isSuccessful: Boolean = status == "1"
)

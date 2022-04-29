package com.dvl.sigma.data.models.data

import com.google.gson.annotations.SerializedName

data class CategoryItem(
    @SerializedName("ct_id") val ctId: String? = null,
    @SerializedName("ct_sort_number") val ctSortNumber: String? = null,
    @SerializedName("ct_name") val ctName: String? = null,
    @SerializedName("ct_desc") val ctDesc: String? = null,
    @SerializedName("ct_image") val ctImage: String? = null,
    @SerializedName("ct_status") val ctStatus: String? = null,
    @SerializedName("isDeleted") val isDeleted: String? = null,
    @SerializedName("created_date") val createdDate: String? = null,
    @SerializedName("updated_date") val updatedDate: String? = null,
)
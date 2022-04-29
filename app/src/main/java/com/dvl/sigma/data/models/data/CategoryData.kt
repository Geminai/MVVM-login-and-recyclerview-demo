package com.dvl.sigma.data.models.data

import com.google.gson.annotations.SerializedName

data class CategoryData(

    @SerializedName("category-list")
    val categoryList: ArrayList<CategoryItem> = arrayListOf()

)

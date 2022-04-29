package com.dvl.sigma.data.network

import com.dvl.sigma.data.models.responses.BaseResponse
import com.dvl.sigma.data.models.responses.CategoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIServices {

    @FormUrlEncoded
    @POST("api/loginme")
    suspend fun login(
        @FieldMap loginRequestMap: HashMap<String, String>
    ): Response<BaseResponse<Any>>

    @FormUrlEncoded
    @POST("api/getAllCategory")
    suspend fun getAllCategory(
        @Header("session_key") sessionKey: String,
        @FieldMap loginRequestMap: HashMap<String, String>
    ): Response<CategoryResponse>

    @Multipart
    @POST("api/my_account")
    suspend fun myAccount(
        @Part("fname") fname: RequestBody,
        @Part("l_name") l_name: RequestBody,
        @Part("city") city: RequestBody,
        @Part("postcode") postcode: RequestBody,
        @Part("contact_number") contact_number: RequestBody,
        @Part profile_pic: MultipartBody.Part
    ): Response<BaseResponse<Any>>

    @FormUrlEncoded
    @POST("api/logout")
    suspend fun logout(
        @Header("session_key") sessionKey: String
    ): Response<BaseResponse<Any>>
}
package com.dvl.sigma.ui.account

import android.content.Context
import com.dvl.sigma.data.models.responses.BaseResponse
import com.dvl.sigma.data.network.APIServices
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class MyAccountRepository @Inject constructor(
    private val apiServices: APIServices
) {

    suspend fun updateMyAccount(
        requestFirstName: RequestBody,
        requestLastName: RequestBody,
        requestCity: RequestBody,
        requestPostcode: RequestBody,
        requestContactNumber: RequestBody,
        filePart: MultipartBody.Part
    ): Response<BaseResponse<Any>> {
        return apiServices.myAccount(
            requestFirstName,
            requestLastName,
            requestCity,
            requestPostcode,
            requestContactNumber,
            filePart
        )
    }

}
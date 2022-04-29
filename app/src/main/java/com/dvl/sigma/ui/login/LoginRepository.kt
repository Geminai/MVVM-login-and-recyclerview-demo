package com.dvl.sigma.ui.login

import android.content.Context
import com.dvl.sigma.data.models.responses.BaseResponse
import com.dvl.sigma.data.network.APIServices
import com.dvl.sigma.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiServices: APIServices,
    @ApplicationContext val context: Context
) {
    suspend fun login(email: String, password: String): Response<BaseResponse<Any>> {

        val loginRequestMap = HashMap<String, String>()

        loginRequestMap["email"] = email
        loginRequestMap["password"] = password
        loginRequestMap["udid"] = Utils.getAndroidSecureId(context).toString()
        loginRequestMap["device_token"] = "ashsggugd7677hdhsdhjj"
        loginRequestMap["device_type"] = "1"

        return apiServices.login(loginRequestMap)
    }
}
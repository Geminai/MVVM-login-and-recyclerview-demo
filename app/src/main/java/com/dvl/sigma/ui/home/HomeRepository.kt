package com.dvl.sigma.ui.home

import com.dvl.sigma.data.PreferencesManager
import com.dvl.sigma.data.models.responses.BaseResponse
import com.dvl.sigma.data.models.responses.CategoryResponse
import com.dvl.sigma.data.network.APIServices
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiServices: APIServices,
    private val preferencesManager: PreferencesManager
) {

    suspend fun getCategories(): CategoryResponse? {

        val categoryRequestMap = HashMap<String, String>()

        categoryRequestMap["offset"] = "0"
        categoryRequestMap["per_page"] = "5"


        return apiServices.getAllCategory(
            preferencesManager.getValueFromKeyFlow(
                PreferencesManager.PreferencesKeys.SESSION_KEY
            ).first(), categoryRequestMap
        ).body()
    }

    suspend fun logout(): Response<BaseResponse<Any>> {

        return apiServices.logout(
            preferencesManager.getValueFromKeyFlow(
                PreferencesManager.PreferencesKeys.SESSION_KEY
            ).first(),
        )
    }

}
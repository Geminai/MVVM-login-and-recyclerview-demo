package com.dvl.sigma.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvl.sigma.data.PreferencesManager
import com.dvl.sigma.data.models.data.UserData
import com.dvl.sigma.data.models.responses.BaseResponse
import com.dvl.sigma.data.models.responses.CategoryResponse
import com.dvl.sigma.data.models.responses.Response
import com.dvl.sigma.utils.NetworkUtils
import com.dvl.sigma.utils.Utils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    @ApplicationContext val context: Context,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val categoryMutableLiveData = MutableLiveData<Response<CategoryResponse>>()

    val categoryLiveData: LiveData<Response<CategoryResponse>> get() = categoryMutableLiveData

    private val logoutMutableLiveData = MutableLiveData<Response<BaseResponse<Any>>>()

    val logoutLiveData: LiveData<Response<BaseResponse<Any>>> get() = logoutMutableLiveData

    init {

        categoryMutableLiveData.value = Response.Loading()

        viewModelScope.launch {

            if (NetworkUtils.isInternetConnected(context)) {
                try {

                    val result = homeRepository.getCategories()

                    if (result!!.status == "1") {

                        result.categoryData.let {

                            categoryMutableLiveData.value = Response.Success(result)

                        }

                    } else {
                        categoryMutableLiveData.value =
                            Response.Error(result.message.toString(), null)
                    }

                } catch (exception: Exception) {
                    categoryMutableLiveData.value =
                        Response.Error(exception.message.toString(), null)
                }

            } else {
                categoryMutableLiveData.value = Response.Error("Internet not available", null)
            }

        }

    }

    fun logout(): LiveData<Response<BaseResponse<Any>>> {

        logoutMutableLiveData.value = Response.Loading()

        viewModelScope.launch {

            if (NetworkUtils.isInternetConnected(context)) {

                try {
                    val result = homeRepository.logout()

                    if (result.isSuccessful) {

                        result.body()?.data.let {

                            preferencesManager.clearData()

                            logoutMutableLiveData.value = Response.Success(null)
                        }

                    } else {
                        logoutMutableLiveData.value =
                            Response.Error(result.body()?.message.toString(), null)
                    }

                } catch (exception: Exception) {
                    logoutMutableLiveData.value = Response.Error(exception.message.toString(), null)
                }

            } else {
                logoutMutableLiveData.value = Response.Error("Internet not available", null)
            }

        }

        return logoutLiveData
    }

}
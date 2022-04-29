package com.dvl.sigma.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvl.sigma.data.PreferencesManager
import com.dvl.sigma.data.models.data.UserData
import com.dvl.sigma.data.models.responses.BaseResponse
import com.dvl.sigma.data.models.responses.Response
import com.dvl.sigma.utils.NetworkUtils
import com.dvl.sigma.utils.Utils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    val preferencesManager: PreferencesManager,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val loginLiveData = MutableLiveData<Response<BaseResponse<Any>>>()

    val login: LiveData<Response<BaseResponse<Any>>> get() = loginLiveData

    var email = ""
    var password = ""

    fun login(): LiveData<Response<BaseResponse<Any>>> {

        loginLiveData.value = Response.Loading()

        viewModelScope.launch {

            if (NetworkUtils.isInternetConnected(context)) {

                try {
                    val result = loginRepository.login(email, password)

                    if (result.isSuccessful) {

                        result.body()?.data.let {

                            val userData =
                                Utils.getObjectFromData(result.body()?.data, UserData::class.java)

                            preferencesManager.setValueForKey(
                                PreferencesManager.PreferencesKeys.IS_LOGIN,
                                true
                            )

                            preferencesManager.setValueForKey(
                                PreferencesManager.PreferencesKeys.SESSION_KEY,
                                userData.sessionKey.toString()
                            )

                            preferencesManager.setValueForKey(
                                PreferencesManager.PreferencesKeys.USER_DATA,
                                Gson().toJson(userData)
                            )

                            loginLiveData.value = Response.Success(result.body())
                        }

                    } else {
                        loginLiveData.value =
                            Response.Error(result.body()?.message.toString(), null)
                    }

                } catch (exception: Exception) {
                    loginLiveData.value = Response.Error(exception.message.toString(), null)
                }

            } else {
                loginLiveData.value = Response.Error("Internet not available", null)
            }

        }

        return login
    }

}
package com.dvl.sigma.ui.account

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvl.sigma.data.PreferencesManager
import com.dvl.sigma.data.models.responses.BaseResponse
import com.dvl.sigma.data.models.responses.Response
import com.dvl.sigma.utils.NetworkUtils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val myAccountRepository: MyAccountRepository,
    val preferencesManager: PreferencesManager,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val updateMyAccountLiveData = MutableLiveData<Response<BaseResponse<Any>>>()

    private val updateMyAccount: LiveData<Response<BaseResponse<Any>>> get() = updateMyAccountLiveData

    var firstName = ""
    var lastName = ""
    var city = ""
    var postcode = ""
    var contactNumber = ""

    private val MEDIA_TYPE_TEXT = "text/plain".toMediaTypeOrNull()
    private val MEDIA_TYPE_IMAGE = "image/*".toMediaTypeOrNull()

    init {

        viewModelScope.launch {

            val userData = preferencesManager.getUser()

            firstName = userData.name.toString()
            lastName = userData.lName.toString()
            city = userData.city.toString()
            postcode = userData.postcode.toString()
            contactNumber = userData.phoneNumber.toString()
        }
    }

    fun updateMyAccount(file: File): LiveData<Response<BaseResponse<Any>>> {

        updateMyAccountLiveData.value = Response.Loading()

        viewModelScope.launch {

            if (NetworkUtils.isInternetConnected(context)) {

                try {

                    val filePart =
                        MultipartBody.Part.createFormData("profile_pic", file.name, RequestBody.create(
                            MEDIA_TYPE_IMAGE, file
                        ))

                    // add another part within the multipart request
                    val requestFirstName: RequestBody = RequestBody.create(
                        MEDIA_TYPE_TEXT, firstName
                    )

                    val requestLastName: RequestBody = RequestBody.create(
                        MEDIA_TYPE_TEXT, lastName
                    )

                    val requestCity: RequestBody = RequestBody.create(
                        MEDIA_TYPE_TEXT, city
                    )

                    val requestPostcode: RequestBody = RequestBody.create(
                        MEDIA_TYPE_TEXT, postcode
                    )

                    val requestContactNumber: RequestBody = RequestBody.create(
                        MEDIA_TYPE_TEXT, contactNumber
                    )

                    val result = myAccountRepository.updateMyAccount(requestFirstName,requestLastName,requestCity,requestPostcode,requestContactNumber,filePart)

                    if (result.isSuccessful) {

                        result.body()?.data.let {

                            updateMyAccountLiveData.value = Response.Success(result.body())
                        }

                    } else {
                        updateMyAccountLiveData.value =
                            Response.Error(result.body()?.message.toString(), null)
                    }

                } catch (exception: Exception) {
                    updateMyAccountLiveData.value =
                        Response.Error(exception.message.toString(), null)
                }

            } else {
                updateMyAccountLiveData.value = Response.Error("Internet not available", null)
            }

        }

        return updateMyAccount
    }

}
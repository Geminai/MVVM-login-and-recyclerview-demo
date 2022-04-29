package com.dvl.sigma.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.gson.Gson

object Utils {

    @SuppressLint("HardwareIds")
    fun getAndroidSecureId(context: Context): String? {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun <T> getObjectFromData(data: Any?, type: Class<T>): T =
        Gson().fromJson(Gson().toJson(data),type)

}
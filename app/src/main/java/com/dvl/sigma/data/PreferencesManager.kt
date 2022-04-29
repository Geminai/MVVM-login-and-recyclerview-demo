package com.dvl.sigma.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dvl.sigma.data.models.data.UserData
import com.dvl.sigma.utils.Constants
import com.google.gson.Gson

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext val context: Context) {

    private val TAG = "PreferencesManager"

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sigma_preference")

    object PreferencesKeys {

        val IS_LOGIN = booleanPreferencesKey(Constants.IS_LOGIN)
        val SESSION_KEY = stringPreferencesKey(Constants.SESSION_KEY)
        val USER_DATA = stringPreferencesKey(Constants.USER_DATA)

    }

    inline fun <reified T> getValueFromKeyFlow(key: Preferences.Key<T>): Flow<T> {

        lateinit var default: Any

        when (T::class) {
            Int::class -> default = 0
            String::class -> default = ""
            Boolean::class -> default = false
        }

        return context.dataStore.data
            .map { preferences ->
                (preferences[key] ?: default) as T
            }

    }

    suspend inline fun <reified T> setValueForKey(key: Preferences.Key<T>, value: Any) {
        context.dataStore.edit { preferences ->
            preferences[key] = value as T
        }
    }

     suspend fun getUser(): UserData =
        Gson().fromJson(
            getValueFromKeyFlow(PreferencesKeys.USER_DATA).first(),
            UserData::class.java
        )


    suspend fun clearData() {
        context.dataStore.edit { it.clear() }
    }


}
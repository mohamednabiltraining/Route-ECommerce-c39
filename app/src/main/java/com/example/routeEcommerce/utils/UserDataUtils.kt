package com.example.routeEcommerce.utils

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.routeEcommerce.Constants

class UserDataUtils {
    fun saveUserInfo(
        context: Context,
        filed: UserDataFiled,
        value: String?,
    ) {
        var masterKey: String? = null
        try {
            masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        } catch (ex: Exception) {
            Log.e("keyEx->", "$ex")
        }

        try {
            // on below line initializing our encrypted
            // shared preferences and passing our key to it.
            val sharedPreferences =
                EncryptedSharedPreferences.create(
                    // passing a file name to share a preferences
                    Constants.USER_DATA,
                    masterKey!!,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
                ) as EncryptedSharedPreferences

            // on below line we are storing data in shared preferences file.
            sharedPreferences.edit().putString(filed.value, value).apply()
        } catch (e: Exception) {
            Log.e("S_P->", "$e")
        }
    }

    fun getUserData(
        context: Context,
        filed: UserDataFiled,
    ): String? {
        // creating a master key for encryption of shared preferences.
        var masterKeyAlias: String? = null
        var neededValue: String? = null
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        // Initialize/open an instance of
        // EncryptedSharedPreferences on below line.
        try {
            // on below line initializing our encrypted
            // shared preferences and passing our key to it.
            val sharedPreferences =
                EncryptedSharedPreferences.create(
                    // passing a file name to share a preferences
                    Constants.USER_DATA,
                    masterKeyAlias!!,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
                ) as EncryptedSharedPreferences

            // on below line creating a variable to
            // get the data from shared prefs.
            neededValue = sharedPreferences.getString(filed.value, null)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return neededValue
    }
}

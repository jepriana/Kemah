package com.ca214.kemah.utils

import android.content.Context
import com.auth0.android.jwt.JWT
import com.ca214.kemah.utils.Constants.ACCESS_TOKEN
import com.ca214.kemah.utils.Constants.REFRESH_TOKEN
import com.ca214.kemah.utils.Constants.SHARED_PREFERENCES_NAME

class TokenManager (context: Context) {
    private val prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
    }

    fun getAccessToken() : String? {
        val accessToken = prefs.getString(ACCESS_TOKEN, null)
        if (accessToken != null) {
            val jwt = JWT(accessToken)
            val isExpired = jwt.isExpired(10)
            if (!isExpired) return accessToken
        }
        return null
    }

    fun saveRefreshToken(token: String) {
        val editor = prefs.edit()
        editor.putString(REFRESH_TOKEN, token)
        editor.apply()
    }

    fun getRefreshToken() : String? {
        val refreshToken = prefs.getString(REFRESH_TOKEN, null)
        if (refreshToken != null) {
            val jwt = JWT(refreshToken)
            val isExpired = jwt.isExpired(10)
            if (!isExpired) return refreshToken
        }
        return null
    }

    fun getUserId() : String? {
        val accessToken = prefs.getString(ACCESS_TOKEN, null)
        if (accessToken != null) {
            val jwt = JWT(accessToken)
            return jwt.getClaim("nameid").asString()
        }
        return null
    }
    fun getUsername() : String? {
        val accessToken = prefs.getString(ACCESS_TOKEN, null)
        if (accessToken != null) {
            val jwt = JWT(accessToken)
            return jwt.getClaim("unique_name").asString()
        }
        return null
    }
}
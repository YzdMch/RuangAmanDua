package com.ubermensch.ruangamandua.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("RuangAmanSession", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID      = "userId"
        private const val KEY_USER_NAME    = "userName"
        private const val KEY_USER_EMAIL   = "userEmail"
        private const val KEY_USER_INSTANSI= "userInstansi"
    }

    fun saveLoginSession(userId: Int, nama: String, email: String, instansi: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, nama)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_INSTANSI, instansi)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""
    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""
    fun getUserInstansi(): String = prefs.getString(KEY_USER_INSTANSI, "") ?: ""
    fun updateUserName(nama: String) = prefs.edit().putString(KEY_USER_NAME, nama).apply()
    fun logout() = prefs.edit().clear().apply()
}
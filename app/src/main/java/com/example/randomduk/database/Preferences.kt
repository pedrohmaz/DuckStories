package com.example.randomduk.database

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

class Preferences(private val application: Application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        "Config",
        Context.MODE_PRIVATE
    )

    fun searchUniqueId(): String {
        var uniqueId = sharedPreferences.getString("uniqueId", null)

        if (uniqueId == null) {
            uniqueId = generateUniqueId()
            sharedPreferences.edit().putString("uniqueId", uniqueId).apply()
        }

        return uniqueId
    }

    private fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }
}
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

    fun buscarIdUnico(): String {
        var idUnico = sharedPreferences.getString("idUnico", null)

        if (idUnico == null) {
            idUnico = gerarIdUnico()
            sharedPreferences.edit().putString("idUnico", idUnico).apply()
        }

        return idUnico
    }

    private fun gerarIdUnico(): String {
        return UUID.randomUUID().toString()
    }
}
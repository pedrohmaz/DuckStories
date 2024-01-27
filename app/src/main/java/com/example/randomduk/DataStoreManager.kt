package com.example.randomduk

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class DataStoreManager(context: Context) {

    companion object {
        @Volatile
        private var instance: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return instance ?: synchronized(this) {
                instance ?: DataStoreManager(context).also { instance = it }
            }
        }
    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("SETTINGS")
    val dataStore = context.dataStore

    object keys {
        val SERVIDOR_ATUALIZADO = booleanPreferencesKey("servidor_atualizado")
    }

    suspend fun saveValue(atualizado: Boolean) {
        dataStore.edit {
            it[keys.SERVIDOR_ATUALIZADO] = atualizado
        }
    }

    fun servidorAtualizado(): Flow<Boolean> {
        return dataStore.data.map {
            it[keys.SERVIDOR_ATUALIZADO] ?: false
        }
    }

}









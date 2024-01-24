package com.example.randomduk.database

import android.app.Application
import android.util.Log
import com.example.randomduk.models.Pato
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Repository(application: Application) {

    private val dao = AppDatabase.getInstance(application).dao()
    private val remoteDb = Firebase.firestore

     fun buscarPatos(): Flow<List<Pato>> {
           return dao.buscaPatos()
    }

    fun savarPato() {}

    fun removerPato() {}

}
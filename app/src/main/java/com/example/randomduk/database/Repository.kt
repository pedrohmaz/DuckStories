package com.example.randomduk.database

import android.app.Application
import com.example.randomduk.DataStoreManager
import com.example.randomduk.models.Pato
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class Repository(application: Application) {

    private val dao = AppDatabase.getInstance(application).dao()
    private val remoteDb = Firebase.firestore

    fun buscarPatos(): Flow<List<Pato>> {
        return dao.buscaPatos()
    }

    fun salvarPato(pato: Pato, scope: CoroutineScope) {
        scope.launch {
            val id = dao.salvarPato(pato)
            val patoMap = mapOf<String, Any?>("url" to pato.url, "name" to pato.nome, "id" to id)
            remoteDb.collection("patos").document("$id").set(patoMap)
        }
    }

    fun removerPato(pato: Pato, scope: CoroutineScope) {
        scope.launch {
            dao.removePato(pato)
            remoteDb.collection("patos").document(pato.id.toString()).delete()
        }
    }




}


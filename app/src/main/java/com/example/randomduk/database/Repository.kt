package com.example.randomduk.database

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.randomduk.models.Pato
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class Repository(private val application: Application) {

    private val dao = AppDatabase.getInstance(application).dao()
    private val remoteDb = Firebase.firestore
    var conectado: Boolean = false
        private set

    init {
        gerenciadorDeInternet()
    }

    fun buscarPatos(): Flow<List<Pato>> {
        return dao.buscaPatos()
    }

    fun salvarPato(pato: Pato, scope: CoroutineScope) {
        scope.launch {
            val id = dao.salvarPato(pato)
            val patoMap = mapOf<String, Any?>("url" to pato.url, "name" to pato.nome, "id" to id)
            remoteDb.collection("patos").document("$id").set(patoMap).addOnFailureListener { }
        }
    }

    fun removerPato(pato: Pato, scope: CoroutineScope) {
        scope.launch {
            dao.removePato(pato)
            remoteDb.collection("patos").document(pato.id.toString()).delete()
        }
    }

    private fun gerenciadorDeInternet() {

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                Log.i("TAG", "onLost: Internet perdida")
                conectado = false
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.i("TAG", "onAvailable: Internet Acessada")
                conectado = true
            }
        }

        val connectivityManager = getSystemService(application, ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)

    }
}


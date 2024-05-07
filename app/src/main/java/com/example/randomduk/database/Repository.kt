package com.example.randomduk.database

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.randomduk.models.Duck
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class Repository(private val application: Application) {

    private val dao = AppDatabase.getInstance(application).dao()
    private val remoteDb = Firebase.firestore
    private val preferences = Preferences(application)
    private val uniqueId = preferences.searchUniqueId()
    var connected: Boolean = false
        private set

    init {
        preferences.searchUniqueId()
        Log.i("TAG", "idUnico: ${preferences.searchUniqueId()}")
        internetManager()
    }

    fun searchDucks(): Flow<List<Duck>> {
        return dao.searchDucks()
    }

    suspend fun searchADuck(id: Int): Duck? {
        return dao.searchADuck(id)
    }

    fun saveDuck(duck: Duck, scope: CoroutineScope) {
        scope.launch {
            val id = dao.saveDuck(duck)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    application,
                    "Pato ${duck.name} salvo",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (connected) {
                val duckMap =
                    mapOf<String, Any?>(
                        "url" to duck.url,
                        "name" to duck.name,
                        "story" to duck.story,
                        "id" to id
                    )
                mapOf<String, Any?>("url" to duck.url, "name" to duck.name, "id" to id)

                remoteDb.collection(uniqueId).document("$id").set(duckMap)
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        application,
                        "Could not update the cloud. No internet connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    fun removeDuck(duck: Duck, scope: CoroutineScope) {
        scope.launch {
            dao.removeDuck(duck)
            if (connected) {
                remoteDb.collection(uniqueId).document(duck.id.toString()).delete()
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        application,
                        "Não foi possível atualizar a nuvem. Internet não disponível",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun internetManager() {

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                Log.i("TAG", "onLost: Internet unavailable")
                connected = false
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.i("TAG", "onAvailable: Internet available")
                connected = true
            }
        }

        val connectivityManager =
            getSystemService(application, ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)

    }
}


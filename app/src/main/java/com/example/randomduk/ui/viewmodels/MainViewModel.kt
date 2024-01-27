package com.example.randomduk.ui.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.DataStoreManager
import com.example.randomduk.data.nomesDePato
import com.example.randomduk.database.AppDatabase
import com.example.randomduk.database.Repository
import com.example.randomduk.models.Pato
import com.example.randomduk.webclient.RetrofitInit
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(application)
    private val dao by lazy { AppDatabase.getInstance(application).dao() }
    private val service = RetrofitInit().service
    private val _url = MutableStateFlow<String?>(null)
    val url: StateFlow<String?> get() = _url
    private val remoteDb = Firebase.firestore
    private val _nomeDoPato = MutableStateFlow<String?>(null)
    val nomeDoPato: StateFlow<String?> get() = _nomeDoPato
    val dataStore = DataStoreManager.getInstance(application)

    init {
        gerarPato()
    }

    fun gerarPato() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = service.chamarPato().execute()
            response.body()?.let {
                withContext(Dispatchers.Main) {
                    _url.value = response.body()!!.url
                }
            }
        }
        _nomeDoPato.value = nomesDePato.random()
    }

    fun salvarPato() {
        val nome: String? = _nomeDoPato.value
        val url = _url.value

        if (!nome.isNullOrEmpty() && !url.isNullOrEmpty()) {
                repo.salvarPato(Pato(url, nome), viewModelScope)
        }
    }


    private fun localToRemote() {

        viewModelScope.launch {
            dao.buscaPatos().collect { patos ->
                patos.forEach {
                    val pato =
                        mapOf<String, Any?>("url" to it.url, "name" to it.nome, "id" to it.id)
                    remoteDb.collection("patos")
                        .add(pato)
                }

            }
        }
    }

}
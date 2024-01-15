package com.example.randomduk.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.models.Pato
import com.example.randomduk.data.nomesDePato
import com.example.randomduk.database.AppDatabase
import com.example.randomduk.webclient.RetrofitInit
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao by lazy { AppDatabase.getInstance(application).dao() }
    private val service = RetrofitInit().service
    private val _url = MutableStateFlow<String?>(null)
    val url: StateFlow<String?> get() = _url
    private val remoteDb = Firebase.firestore

    private val _nomeDoPato = MutableStateFlow<String?>(null)
    val nomeDoPato: StateFlow<String?> get() = _nomeDoPato

    init {
        gerarPato()
        localToRemote(application)
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
            viewModelScope.launch {
                dao.salvarPato(Pato(nome = nome, url = url))
            }
        }
    }

    private fun localToRemote(context: Application) {
        val pato = mapOf<String, String?>("url" to null, "name" to "Douglas")
        viewModelScope.launch {
            val patosCollection = remoteDb.collection("patos")
            patosCollection
                .add(pato)
                .addOnSuccessListener {
                    Toast.makeText(context, "Servidor remoro atualizado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Erro ao atualizar servidor remoto", Toast.LENGTH_SHORT)
                        .show()
                }
        }

    }
}


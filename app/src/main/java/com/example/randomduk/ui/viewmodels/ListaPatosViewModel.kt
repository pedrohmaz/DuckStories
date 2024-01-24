package com.example.randomduk.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.database.AppDatabase
import com.example.randomduk.database.Repository
import com.example.randomduk.models.Pato
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaPatosViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(application)
    private val dao by lazy { AppDatabase.getInstance(application).dao() }
    private val remoteDb = Firebase.firestore
    private val _listaPatos = MutableStateFlow<List<Pato>>(emptyList())
    val listaPatos: StateFlow<List<Pato>> get() = _listaPatos

    init {
        buscarPatos()
    }

    fun buscarPatos() {
          viewModelScope.launch {
              repo.buscarPatos().collect{
                  _listaPatos.value = it
              }
          }
    }

    fun removerPato(pato: Pato) {
        viewModelScope.launch {
            dao.removePato(pato)
            withContext(Dispatchers.IO) {
                remoteDb.collection("patos").document(pato.id.toString()).delete()
            }
        }
    }

    fun salvarPato(pato: Pato) {
        viewModelScope.launch {
            dao.salvarPato(pato)
            remoteDb.collection("patos").document(pato.id.toString()).set(pato)
        }
    }
}
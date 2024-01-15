package com.example.randomduk.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.models.Pato
import com.example.randomduk.database.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListaPatosViewModel(application: Application) : AndroidViewModel(application) {

    private val dao by lazy { AppDatabase.getInstance(application).dao() }
    private val _listaPatos = MutableStateFlow<List<Pato>?>(null)
    val listaPatos: StateFlow<List<Pato>?> get() = _listaPatos

    init {
        buscarPatos()
    }

    private fun buscarPatos() {
        viewModelScope.launch {
            dao.buscaPatos().collect {
                _listaPatos.value = it
            }
        }
    }

    fun removerPato(pato: Pato) {
        viewModelScope.launch {
            dao.removePato(pato)
        }
    }

    fun salvarPato(pato: Pato) {
        viewModelScope.launch {
            dao.salvarPato(pato)
        }
    }
}
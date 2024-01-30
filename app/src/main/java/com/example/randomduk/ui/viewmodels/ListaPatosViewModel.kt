package com.example.randomduk.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.database.Repository
import com.example.randomduk.models.Pato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListaPatosViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(application)
    private val _listaPatos = MutableStateFlow<List<Pato>>(emptyList())
    val listaPatos: StateFlow<List<Pato>> get() = _listaPatos

    init {
        buscarPatos()
    }

    private fun buscarPatos() {
        viewModelScope.launch {
            repo.buscarPatos().collect {
                _listaPatos.value = it
            }
        }
    }

    fun removerPato(pato: Pato) {
        repo.removerPato(pato, viewModelScope)
    }

    fun salvarPato(pato: Pato) {
        repo.salvarPato(pato, viewModelScope)
    }

}


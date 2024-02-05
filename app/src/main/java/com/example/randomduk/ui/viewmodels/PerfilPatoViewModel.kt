package com.example.randomduk.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.database.Repository
import com.example.randomduk.models.Pato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PerfilPatoViewModel(application: Application) : AndroidViewModel(application) {

    private val repo by lazy { Repository(application) }
    var id: Int? = null

    private val _pato = MutableStateFlow<Pato?>(null)
    val pato: StateFlow<Pato?> get() = _pato

    init {
        buscaUmPato()
    }

    private fun buscaUmPato() {
        viewModelScope.launch {
                _pato.value = id?.let { repo.buscarUmPato(id!!) }
        }
    }

    fun editaPato(pato: Pato) {
        viewModelScope.launch {
            repo.salvarPato(pato, viewModelScope)
        }
        _pato.value = pato
    }

}
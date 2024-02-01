package com.example.randomduk.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.database.Repository
import com.example.randomduk.models.Pato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

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

    fun salvaPato(pato: Pato) {
        repo.salvarPato(pato, viewModelScope).let {
            viewModelScope.launch {
                _pato.value = repo.buscarUmPato(pato.id)
            }
        }
    }

}
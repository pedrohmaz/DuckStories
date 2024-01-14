package com.example.randomduk.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.randomduk.Pato
import com.example.randomduk.data.nomesDePato
import com.example.randomduk.database.AppDatabase
import com.example.randomduk.webclient.RetrofitInit
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

    private val _nomeDoPato = MutableStateFlow<String?>(null)
    val nomeDoPato: StateFlow<String?> get() = _nomeDoPato

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
            viewModelScope.launch {
                dao.salvarPato(Pato(nome = nome, url = url))
            }
        }
    }
}

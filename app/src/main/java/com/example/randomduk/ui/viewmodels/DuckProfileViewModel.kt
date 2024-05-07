package com.example.randomduk.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.database.Repository
import com.example.randomduk.models.Duck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DuckProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repo by lazy { Repository(application) }
    var id: Int? = null

    private val _duck = MutableStateFlow<Duck?>(null)
    val duck: StateFlow<Duck?> get() = _duck

    init {
        searchADuck()
    }

    private fun searchADuck() {
        viewModelScope.launch {
                _duck.value = id?.let { repo.searchADuck(id!!) }
        }
    }

    fun editDuck(duck: Duck) {
        viewModelScope.launch {
            repo.saveDuck(duck, viewModelScope)
        }
        _duck.value = duck
    }

}
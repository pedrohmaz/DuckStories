package com.example.randomduk.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.database.Repository
import com.example.randomduk.models.Duck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DuckListViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(application)
    private val _duckList = MutableStateFlow<List<Duck>>(emptyList())
    val duckList: StateFlow<List<Duck>> get() = _duckList

    init {
        searchDucks()
    }

    private fun searchDucks() {
        viewModelScope.launch {
            repo.searchDucks().collect {
                _duckList.value = it
            }
        }
    }

    fun removeDuck(duck: Duck) {
        repo.removeDuck(duck, viewModelScope)
    }

    fun saveDuck(duck: Duck) {
        repo.saveDuck(duck, viewModelScope)
    }

}


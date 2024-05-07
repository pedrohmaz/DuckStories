package com.example.randomduk.ui.viewmodels

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomduk.data.fraseInicialDoPato
import com.example.randomduk.data.duckPlaces
import com.example.randomduk.data.duckNames
import com.example.randomduk.database.Repository
import com.example.randomduk.models.Duck
import com.example.randomduk.webclient.RetrofitInit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val application: Application) : AndroidViewModel(application) {

    private val repo = Repository(application)
    private val service = RetrofitInit().service
    private val _url = MutableStateFlow<String?>(null)
    val url: StateFlow<String?> get() = _url
    private val _duckName = MutableStateFlow<String?>(null)
    val duckName: StateFlow<String?> get() = _duckName
    private val _story = MutableStateFlow("")
    val story: StateFlow<String> get() = _story


    init {
        generateDuck()
    }

    fun generateDuck() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repo.connected) {
                val response = service.callDuck().execute()
                response.body()?.let {
                    withContext(Dispatchers.Main) {
                        _url.value = response.body()!!.url
                    }
                }
                _duckName.value = duckNames.random()
                _story.value = "${_duckName.value} was born ${duckPlaces.random()}, ${fraseInicialDoPato.random()}..."

            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        application,
                        "Could not generate duck. Internet unavailable.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

        fun saveDuck() {
            val name: String? = _duckName.value
            val url = _url.value
            val story = _story.value
            if (!name.isNullOrEmpty() && !url.isNullOrEmpty()) {
                repo.saveDuck(Duck(url, name, story), viewModelScope)
            } else {
                Toast.makeText(application, "Unable to save duck.", Toast.LENGTH_SHORT).show()
            }
        }


}
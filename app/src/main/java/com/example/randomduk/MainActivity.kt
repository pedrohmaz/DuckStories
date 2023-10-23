package com.example.randomduk

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.randomduk.databinding.ActivityMainBinding
import com.example.randomduk.webclient.RetrofitInit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val service = RetrofitInit().service



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        gerarPato()

        binding.botaoPato.setOnClickListener {
            val quackSound = MediaPlayer.create(this, R.raw.quack_sound)
            quackSound.start()
            gerarPato()
        }

    }

    private fun gerarPato() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = service.quack().execute()
            response.body()?.let {
                withContext(Main) {
                   var url = response.body()!!.url
                    Glide.with(this@MainActivity).load(url).into(binding.imagemPato)
                }
            }
        }
    }
}
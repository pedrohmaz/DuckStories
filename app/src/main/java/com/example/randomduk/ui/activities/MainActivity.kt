package com.example.randomduk.ui.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.randomduk.R
import com.example.randomduk.data.duckNames
import com.example.randomduk.databinding.ActivityMainBinding
import com.example.randomduk.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        vmSetup()
        onQuackButtonClick()
    }


    private fun vmSetup() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        Log.i("TAG", "onCreate: ${duckNames.size}")

        lifecycleScope.launch {
            viewModel.url.collect { imageUrl ->
                imageUrl?.let {
                    Glide.with(this@MainActivity).load(it).into(binding.imagemPato)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.duckName.collect { nomeDoPato ->
                binding.nomeDoPato.text = nomeDoPato
            }
        }
        lifecycleScope.launch {
            viewModel.story.collect {
                binding.historiaPato.text = it
            }
        }
    }


    private fun onQuackButtonClick() {
        binding.botaoPato.setOnClickListener {
            val quackSound = MediaPlayer.create(this, R.raw.quack_sound)
            quackSound.start()
            viewModel.generateDuck()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.salvar_pato -> {
                viewModel.saveDuck()
                true
            }

            R.id.lista_patos -> {
                startActivity(Intent(this, DuckListActivity::class.java))
                true
            }

            else -> false
        }

    }

}


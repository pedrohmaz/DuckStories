package com.example.randomduk

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.randomduk.data.nomesDePato
import com.example.randomduk.databinding.ActivityMainBinding
import com.example.randomduk.viewmodels.MainViewModel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        vmSetup()
        aoClicarBotaoQuack()
    }


    private fun vmSetup() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        Log.i("TAG", "onCreate: ${nomesDePato.size}")

        lifecycleScope.launch {
            viewModel.url.collect { imageUrl ->
                imageUrl?.let {
                    Glide.with(this@MainActivity).load(it).into(binding.imagemPato)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.nomeDoPato.collect { nomeDoPato ->
                binding.nomeDoPato.text = nomeDoPato
            }
        }

    }


    private fun aoClicarBotaoQuack() {
        binding.botaoPato.setOnClickListener {
            val quackSound = MediaPlayer.create(this, R.raw.quack_sound)
            quackSound.start()
            viewModel.gerarPato()
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
                viewModel.salvarPato()
                Toast.makeText(this, "Pato salvo", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.lista_patos -> {
                startActivity(Intent(this, ListaPatosActivity::class.java))
                true
            }

            else -> false
        }

    }

}


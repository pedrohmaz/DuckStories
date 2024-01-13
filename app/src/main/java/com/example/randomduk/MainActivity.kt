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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.randomduk.data.nomesDePato
import com.example.randomduk.database.AppDatabase
import com.example.randomduk.databinding.ActivityMainBinding
import com.example.randomduk.webclient.RetrofitInit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val db by lazy { AppDatabase.getInstance(this) }
    private val dao by lazy {db.dao()}
    private val service = RetrofitInit().service
    private var url: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.i("TAG", "onCreate: ${nomesDePato.size}")
        gerarPato()
        aoClicarBotaoQuack()

    }

    private fun aoClicarBotaoQuack() {
        binding.botaoPato.setOnClickListener {
            val quackSound = MediaPlayer.create(this, R.raw.quack_sound)
            quackSound.start()
            gerarPato()
        }
    }

    private fun gerarPato() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = service.chamarPato().execute()
            response.body()?.let {
                withContext(Main) {
                    this@MainActivity.url  = response.body()!!.url
                    Glide.with(this@MainActivity).load(url).into(binding.imagemPato)
                }
            }
        }
        binding.nomeDoPato.text = nomesDePato.random()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId) {
           R.id.salvar_pato -> {
               val nome = binding.nomeDoPato.text.toString()
               val url = url
               lifecycleScope.launch {
               dao.salvarPato(Pato(nome = nome, url = url))}
               Toast.makeText(this, "Pato salvo", Toast.LENGTH_SHORT).show()
               true
           }
           R.id.lista_patos -> {
               startActivity(Intent(this, ListaPatosActivity::class.java))
               true }
           else -> false
       }

    }

}


package com.example.randomduk.ui.activities

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.randomduk.database.Repository
import com.example.randomduk.databinding.ActivityPerfilPatoBinding
import com.example.randomduk.models.Pato
import kotlinx.coroutines.launch

class PerfilPatoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPerfilPatoBinding.inflate(layoutInflater) }
    private val repo by lazy {Repository(application)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var pato: Pato?
        lifecycleScope.launch {
            val id = intent.getIntExtra("ID_KEY", 666)
            Log.i("TAG", "onCreate: $id")
            pato = repo.buscarUmPato(id)
            Log.i("TAG", "onCreate: $pato")
            binding.perfilNomeDoPato.text = pato?.nome
            binding.perfilHistoriaDoPato.text = pato?.historia
            Glide.with(this@PerfilPatoActivity).load(pato?.url).into(binding.perfilFotoDoPato)
        }
    }

}
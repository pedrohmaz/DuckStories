package com.example.randomduk

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomduk.databinding.ActivityListaPatosBinding

class ListaPatosActivity: AppCompatActivity() {

    val binding by lazy {ActivityListaPatosBinding.inflate(layoutInflater)}


    override fun onStart() {
        super.onStart()
        setContentView(binding.root)
        val rvPatos = binding.patosRv
        val adapter = PatosRvAdapter(
            listOf(
                Pato(nome = "Gerson"), Pato(nome = "Sandra"), Pato(nome = "Felipe")
            ) )
        rvPatos.adapter = adapter
        rvPatos.layoutManager = LinearLayoutManager(this)

    }


}
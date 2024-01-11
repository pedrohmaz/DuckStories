package com.example.randomduk

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomduk.database.AppDatabase
import com.example.randomduk.databinding.ActivityListaPatosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaPatosActivity: AppCompatActivity() {

    private val binding by lazy {ActivityListaPatosBinding.inflate(layoutInflater)}
    private val db by lazy { AppDatabase.getInstance(this) }
    private val dao by lazy {db.dao()}


    override fun onStart() {
        super.onStart()
        setContentView(binding.root)
        val rvPatos = binding.patosRv

        lifecycleScope.launch(Dispatchers.IO){
            val adapter = PatosRvAdapter(dao.buscaPatos(), this@ListaPatosActivity)
            rvPatos.adapter = adapter
        }
        rvPatos.layoutManager = LinearLayoutManager(this)

    }


}
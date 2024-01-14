package com.example.randomduk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomduk.database.AppDatabase
import com.example.randomduk.databinding.ActivityListaPatosBinding
import com.example.randomduk.viewmodels.ListaPatosViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ListaPatosActivity : AppCompatActivity() {

    private val binding by lazy { ActivityListaPatosBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[ListaPatosViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        rvSetup()
    }

    private fun rvSetup() {
        val rvPatos = binding.patosRv
        val adapter = PatosRvAdapter(this@ListaPatosActivity)
        lifecycleScope.launch {
            viewModel.listaPatos.observe(this@ListaPatosActivity){
                adapter.patos = it
                adapter.notifyDataSetChanged()
            }
        }
        rvPatos.adapter = adapter
        rvPatos.layoutManager = LinearLayoutManager(this@ListaPatosActivity)
        swipeController(adapter, rvPatos)
    }

    private fun swipeController(
        adapter: PatosRvAdapter,
        rv: RecyclerView
    ) {
        val swipeDelete = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val item = adapter.patos[pos]
                viewModel.removerPato(item)
                Snackbar.make(
                    binding.root,
                    "Pato '${adapter.patos[pos].nome}' removido",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Undo") {
                        viewModel.salvarPato(item)
                    }
                    show()
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeDelete)
        touchHelper.attachToRecyclerView(rv)
    }
}

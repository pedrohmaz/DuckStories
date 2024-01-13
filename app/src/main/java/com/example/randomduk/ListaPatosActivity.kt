package com.example.randomduk

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomduk.database.AppDatabase
import com.example.randomduk.databinding.ActivityListaPatosBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ListaPatosActivity : AppCompatActivity() {

    private val binding by lazy { ActivityListaPatosBinding.inflate(layoutInflater) }
    private val db by lazy { AppDatabase.getInstance(this) }
    private val dao by lazy { db.dao() }


    override fun onStart() {
        super.onStart()
        setContentView(binding.root)
        rvSetup()
    }

    private fun rvSetup() {
        val rvPatos = binding.patosRv
       lateinit var adapter: PatosRvAdapter
        lifecycleScope.launch {
            dao.buscaPatos().collect {
                    adapter = PatosRvAdapter(it, this@ListaPatosActivity)
                    rvPatos.adapter = adapter
                }
            }

        val swipeDelete = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val item = adapter.patos[pos]
                adapter.removerItem(pos, lifecycleScope)
                Snackbar.make(
                    binding.root,
                    "Pato '${adapter.patos[pos].nome}' removido",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Undo") {
                        adapter.adicionarItem(item, lifecycleScope)
                    }
                    show()
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeDelete)
        touchHelper.attachToRecyclerView(rvPatos)
        rvPatos.layoutManager = LinearLayoutManager(this@ListaPatosActivity)
    }
}


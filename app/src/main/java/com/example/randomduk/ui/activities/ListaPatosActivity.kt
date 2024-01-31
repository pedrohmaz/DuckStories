package com.example.randomduk.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomduk.SwipeToDeleteCallback
import com.example.randomduk.databinding.ActivityListaPatosBinding
import com.example.randomduk.ui.PatosRvAdapter
import com.example.randomduk.ui.viewmodels.ListaPatosViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ListaPatosActivity : AppCompatActivity(), PatosRvAdapter.OnItemCLickListener {

    private val binding by lazy { ActivityListaPatosBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[ListaPatosViewModel::class.java] }
    val adapter = PatosRvAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        rvSetup()
    }


    private fun rvSetup() {
        val rvPatos = binding.patosRv

        lifecycleScope.launch {
            viewModel.listaPatos.collect {
                adapter.patos.value = it
                rvPatos.adapter = adapter
            }
        }
        rvPatos.layoutManager = LinearLayoutManager(this)
        swipeController(rvPatos)
        adapter.onItemCLickListener = this
    }

    override fun onItemClick(position: Int) {
        val id = adapter.patos.value[position].id
        intent = Intent(this, PerfilPatoActivity::class.java)
        intent.putExtra("ID_KEY", id)
        Log.i("TAG", "onItemClick: $id")
        startActivity(intent)
    }

    private fun swipeController(
        rv: RecyclerView
    ) {
        val swipeDelete = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val item = adapter.patos.value[pos]
                viewModel.removerPato(item)
                Snackbar.make(
                    binding.root,
                    "Pato '${item.nome}' removido",
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

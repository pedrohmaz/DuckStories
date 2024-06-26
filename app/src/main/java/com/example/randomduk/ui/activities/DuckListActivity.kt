package com.example.randomduk.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomduk.SwipeToDeleteCallback
import com.example.randomduk.databinding.ActivityListaPatosBinding
import com.example.randomduk.ui.DucksRvAdapter
import com.example.randomduk.ui.viewmodels.DuckListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DuckListActivity : AppCompatActivity(), DucksRvAdapter.OnItemCLickListener {

    private val binding by lazy { ActivityListaPatosBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[DuckListViewModel::class.java] }
    val adapter = DucksRvAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        rvSetup()
    }


    private fun rvSetup() {
        val rvDucks = binding.patosRv

        lifecycleScope.launch {
            viewModel.duckList.collect {
                adapter.ducks.value = it
                rvDucks.adapter = adapter
            }
        }
        rvDucks.layoutManager = LinearLayoutManager(this)
        swipeController(rvDucks)
        adapter.onItemCLickListener = this
    }

    override fun onItemClick(position: Int) {
        val id = adapter.ducks.value[position].id
        intent = Intent(this, DuckProfileActivity::class.java)
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
                val item = adapter.ducks.value[pos]
                viewModel.removeDuck(item)
                Snackbar.make(
                    binding.root,
                    "Duck '${item.name}' removed",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Undo") {
                        viewModel.saveDuck(item)
                    }
                    show()
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeDelete)
        touchHelper.attachToRecyclerView(rv)
    }
}

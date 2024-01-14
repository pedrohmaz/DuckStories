package com.example.randomduk


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomduk.database.AppDatabase
import com.example.randomduk.databinding.ListaPatosRecyclerViewItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PatosRvAdapter(private val context: Context) :
    RecyclerView.Adapter<PatosRvAdapter.ViewHolder>() {

    var patos = MutableStateFlow<List<Pato>>(emptyList())

    inner class ViewHolder(binding: ListaPatosRecyclerViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        val nomeDoPato: TextView = binding.listaNomeDoPato
        var fotoDoPato: ImageView = binding.listaFotoDoPato
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListaPatosRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return patos.value.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nomeDoPato.text = patos.value[position].nome
        Glide.with(context).load(patos.value[position].url).into(holder.fotoDoPato)
    }

}
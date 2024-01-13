package com.example.randomduk


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomduk.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatosRvAdapter(val patos: List<Pato>, private val context: Context) :
    RecyclerView.Adapter<PatosRvAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeDoPato: TextView = itemView.findViewById(R.id.listaNomeDoPato)
        var fotoDoPato: ImageView = itemView.findViewById(R.id.listaFotoDoPato)
    }


    fun removerItem(i: Int, s: CoroutineScope) {
        s.launch {
            AppDatabase.getInstance(context).dao().removePato(patos[i])
        }
    }

    fun adicionarItem(p: Pato, s: CoroutineScope) {
        s.launch {
            AppDatabase.getInstance(context).dao().salvarPato(p)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lista_patos_recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return patos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.nomeDoPato.text = patos[position].nome
        Glide.with(context).load(patos[position].url).into(holder.fotoDoPato)


    }

}
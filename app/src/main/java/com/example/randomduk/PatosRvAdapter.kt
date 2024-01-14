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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class PatosRvAdapter(private val context: Context) :
    RecyclerView.Adapter<PatosRvAdapter.ViewHolder>() {

    var patos: List<Pato> = emptyList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeDoPato: TextView = itemView.findViewById(R.id.listaNomeDoPato)
        var fotoDoPato: ImageView = itemView.findViewById(R.id.listaFotoDoPato)
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
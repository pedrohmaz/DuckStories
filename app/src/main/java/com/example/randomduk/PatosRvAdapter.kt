package com.example.randomduk


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

 class PatosRvAdapter(private val patos: List<Pato>): RecyclerView.Adapter<PatosRvAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nomeDoPato: TextView = itemView.findViewById(R.id.listaNomeDoPato)
        var fotoDoPato: ImageView = itemView.findViewById(R.id.listaFotoDoPato)
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val view = LayoutInflater.from(parent.context)
             .inflate(R.layout.lista_patos_recycler_view_item, parent, false)
         return ViewHolder(view)
     }

     override fun getItemCount(): Int {
         Log.i("TAG", "getItemCount: $patos")
         return patos.size
     }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.nomeDoPato.text = patos[position].nome
         holder.fotoDoPato.setImageResource(R.drawable.ic_launcher_foreground)
     }

 }
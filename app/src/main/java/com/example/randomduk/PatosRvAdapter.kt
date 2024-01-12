package com.example.randomduk


import android.content.Context
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatosRvAdapter(val patos: MutableList<Pato>, private val context: Context) :
    RecyclerView.Adapter<PatosRvAdapter.ViewHolder>() {

    //val publicPatos: List<Pato> get() = patos

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeDoPato: TextView = itemView.findViewById(R.id.listaNomeDoPato)
        var fotoDoPato: ImageView = itemView.findViewById(R.id.listaFotoDoPato)
        val botaoRemover: ImageButton = itemView.findViewById(R.id.botaoRemoverPato)

    }

    fun removerItem(i: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getInstance(context).dao().removePato(patos[i])
            withContext(Dispatchers.Main) {
                patos.removeAt(i)
                notifyDataSetChanged()
            }
        }
    }

    fun adicionarItem(p: Pato, i: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            patos.add(i, p)
            notifyDataSetChanged()
            withContext(Dispatchers.IO) {
                AppDatabase.getInstance(context).dao().salvarPato(p)
            }
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
        holder.botaoRemover.setOnClickListener { removerItem(position) }

    }

}
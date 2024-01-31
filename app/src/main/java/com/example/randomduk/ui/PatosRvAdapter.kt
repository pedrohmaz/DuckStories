package com.example.randomduk.ui


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomduk.databinding.ListaPatosRecyclerViewItemBinding
import com.example.randomduk.models.Pato
import kotlinx.coroutines.flow.MutableStateFlow


class PatosRvAdapter(private val context: Context) :
    RecyclerView.Adapter<PatosRvAdapter.ViewHolder>() {

     val patos = MutableStateFlow<List<Pato>>(emptyList())
     var onItemCLickListener: OnItemCLickListener? = null

    interface OnItemCLickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(binding: ListaPatosRecyclerViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener{
                onItemCLickListener?.onItemClick(adapterPosition)
            }
        }
        val nomeDoPato: TextView = binding.listaNomeDoPato
        var fotoDoPato: ImageView = binding.listaFotoDoPato
        var historia: TextView = binding.listaHistoriaDoPato


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
        holder.historia.text = patos.value[position].historia
        Glide.with(context).load(patos.value[position].url).into(holder.fotoDoPato)
    }


}
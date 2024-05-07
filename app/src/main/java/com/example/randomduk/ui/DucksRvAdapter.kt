package com.example.randomduk.ui


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomduk.databinding.ListaPatosRecyclerViewItemBinding
import com.example.randomduk.models.Duck
import kotlinx.coroutines.flow.MutableStateFlow


class DucksRvAdapter(private val context: Context) :
    RecyclerView.Adapter<DucksRvAdapter.ViewHolder>() {

     val ducks = MutableStateFlow<List<Duck>>(emptyList())
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
        val duckName: TextView = binding.listaNomeDoPato
        var duckPic: ImageView = binding.listaFotoDoPato
        var duckStory: TextView = binding.listaHistoriaDoPato


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListaPatosRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ducks.value.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.duckName.text = ducks.value[position].name
        holder.duckStory.text = ducks.value[position].story
        Glide.with(context).load(ducks.value[position].url).into(holder.duckPic)
    }


}
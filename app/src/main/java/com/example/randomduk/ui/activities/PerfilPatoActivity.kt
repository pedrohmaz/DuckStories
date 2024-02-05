package com.example.randomduk.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.randomduk.R
import com.example.randomduk.databinding.ActivityPerfilPatoBinding
import com.example.randomduk.models.Pato
import com.example.randomduk.ui.viewmodels.PerfilPatoViewModel
import kotlinx.coroutines.launch

class PerfilPatoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPerfilPatoBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[PerfilPatoViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            viewModel.id = intent.getIntExtra("ID_KEY", 666)
            viewModel.pato.collect { pato ->
                binding.perfilNomeDoPato.text = pato?.nome
                binding.perfilHistoriaDoPato.text = pato?.historia
                Glide.with(this@PerfilPatoActivity).load(pato?.url).into(binding.perfilFotoDoPato)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.perfil_pato_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.salvar_pato -> {
                val editText = EditText(this)
                AlertDialog.Builder(this)
                .setTitle("Editar história")
                .setView(editText.apply{this.setText(viewModel.pato.value?.historia)})
                .setNegativeButton("Cancelar"){ _,_ -> }
                .setPositiveButton("Salvar"){ _,_ ->
                    viewModel.editaPato(Pato(
                        viewModel.pato.value?.url,
                        viewModel.pato.value!!.nome,
                        editText.text.toString(),
                        viewModel.pato.value!!.id
                        )
                    )
                }
                .show()
                true
            }

            R.id.lista_patos -> {
                val intent = Intent(this, ListaPatosActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

}



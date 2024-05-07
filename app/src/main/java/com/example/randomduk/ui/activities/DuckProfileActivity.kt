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
import com.example.randomduk.models.Duck
import com.example.randomduk.ui.viewmodels.DuckProfileViewModel
import kotlinx.coroutines.launch

class DuckProfileActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPerfilPatoBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[DuckProfileViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            viewModel.id = intent.getIntExtra("ID_KEY", 666)
            viewModel.duck.collect { pato ->
                binding.perfilNomeDoPato.text = pato?.name
                binding.perfilHistoriaDoPato.text = pato?.story
                Glide.with(this@DuckProfileActivity).load(pato?.url).into(binding.perfilFotoDoPato)
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
                .setView(editText.apply{this.setText(viewModel.duck.value?.story)})
                .setNegativeButton("Cancelar"){ _,_ -> }
                .setPositiveButton("Salvar"){ _,_ ->
                    viewModel.editDuck(Duck(
                        viewModel.duck.value?.url,
                        viewModel.duck.value!!.name,
                        editText.text.toString(),
                        viewModel.duck.value!!.id
                        )
                    )
                }
                .show()
                true
            }

            R.id.lista_patos -> {
                val intent = Intent(this, DuckListActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

}



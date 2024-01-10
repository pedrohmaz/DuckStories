package com.example.randomduk

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pato(
    val url: String?,
    val nome: String,
    @PrimaryKey(autoGenerate = true) val id: Int

)
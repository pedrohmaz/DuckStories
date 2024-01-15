package com.example.randomduk.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pato(
    val url: String? = null,
    val nome: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

)
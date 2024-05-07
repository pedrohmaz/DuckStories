package com.example.randomduk.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Duck(
    val url: String? = null,
    val name: String,
    var story: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

)
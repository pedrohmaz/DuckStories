package com.example.randomduk.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.randomduk.Pato

@Dao
interface Dao {

    @Query("SELECT * FROM pato")
    fun buscaPatos(): List<Pato>

    @Insert
    fun salvarPato(pato: Pato)

}
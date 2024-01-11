package com.example.randomduk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.randomduk.Pato

@Dao
interface Dao {

    @Query("SELECT * FROM pato")
    fun buscaPatos(): MutableList<Pato>

    @Insert
    fun salvarPato(pato: Pato)

    @Delete
    fun removePato(pato: Pato)

}
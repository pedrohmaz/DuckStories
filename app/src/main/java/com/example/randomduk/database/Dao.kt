package com.example.randomduk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.randomduk.models.Pato
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM pato")
    fun buscaPatos(): Flow<List<Pato>>

    @Query("SELECT * FROM pato WHERE id = :id" )
    suspend fun buscaUmPato(id: Int): Pato?

    @Insert
    suspend fun salvarPato(pato: Pato): Long

    @Delete
    suspend fun removePato(pato: Pato)



}
package com.example.randomduk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.randomduk.models.Duck
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM duck")
    fun searchDucks(): Flow<List<Duck>>

    @Query("SELECT * FROM duck WHERE id = :id" )
    suspend fun searchADuck(id: Int): Duck?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDuck(duck: Duck): Long

    @Delete
    suspend fun removeDuck(duck: Duck)



}
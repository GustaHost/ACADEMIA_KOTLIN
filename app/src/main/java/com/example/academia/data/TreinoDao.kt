package com.example.academia.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TreinoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreino(treino: Treino)

    @Update
    suspend fun updateTreino(treino: Treino)

    @Delete
    suspend fun deleteTreino(treino: Treino)

    @Query("SELECT * FROM tabela_treinos ORDER BY treinoId DESC")
    fun getAllTreinos(): Flow<List<Treino>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercicio(exercicio: Exercicio)

    @Update
    suspend fun updateExercicio(exercicio: Exercicio)

    @Delete
    suspend fun deleteExercicio(exercicio: Exercicio)

    @Query("SELECT * FROM tabela_exercicios WHERE treinoFk = :treinoId ORDER BY exercicioId ASC")
    fun getExerciciosByTreino(treinoId: Int): Flow<List<Exercicio>>
}
package com.example.academia.repository

import com.example.academia.data.Exercicio
import com.example.academia.data.Treino
import com.example.academia.data.TreinoDao
import com.example.academia.model.DicaDoDia
import com.example.academia.network.ApiService
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class AppRepository(
    private val treinoDao: TreinoDao,
    private val apiService: ApiService
) {
    // Funções CRUD Treino
    fun getAllTreinos(): Flow<List<Treino>> = treinoDao.getAllTreinos()
    suspend fun insertTreino(treino: Treino) = treinoDao.insertTreino(treino)
    suspend fun deleteTreino(treino: Treino) = treinoDao.deleteTreino(treino)
    suspend fun updateTreino(treino: Treino) = treinoDao.updateTreino(treino)

    // Funções CRUD Exercicio
    fun getExerciciosByTreino(treinoId: Int): Flow<List<Exercicio>> = treinoDao.getExerciciosByTreino(treinoId)
    suspend fun insertExercicio(exercicio: Exercicio) = treinoDao.insertExercicio(exercicio)
    suspend fun deleteExercicio(exercicio: Exercicio) = treinoDao.deleteExercicio(exercicio)
    suspend fun updateExercicio(exercicio: Exercicio) = treinoDao.updateExercicio(exercicio)

    // Função API com a API catfact.ninja
    suspend fun getDicaDoDia(): DicaDoDia {
        val response = apiService.getDicaDoDia()
        if (!response.isSuccessful) {
            throw IOException("Falha na chamada à API com o código: ${response.code()}")
        }
        return response.body() ?: throw IOException("A resposta da API está vazia.")
    }
}
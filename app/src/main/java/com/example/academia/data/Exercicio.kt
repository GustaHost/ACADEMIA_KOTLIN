package com.example.academia.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_exercicios")
data class Exercicio(
    @PrimaryKey(autoGenerate = true)
    val exercicioId: Int = 0,
    val treinoFk: Int, // Chave estrangeira
    val nomeExercicio: String,
    val repeticoes: Int,
    val series: Int
)
package com.example.academia.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_treinos")
data class Treino(
    @PrimaryKey(autoGenerate = true)
    val treinoId: Int = 0,
    val nome: String,
    val data: String,
    val descricao: String
)
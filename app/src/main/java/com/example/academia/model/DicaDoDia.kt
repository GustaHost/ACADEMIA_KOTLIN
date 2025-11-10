package com.example.academia.model

import com.google.gson.annotations.SerializedName

// Modelo para a API catfact.ninja
data class DicaDoDia(
    @SerializedName("fact")
    val fact: String
)

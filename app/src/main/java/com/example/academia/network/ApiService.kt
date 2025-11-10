package com.example.academia.network

import com.example.academia.model.DicaDoDia
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Endpoint para a API de fatos sobre gatos (catfact.ninja)
    @GET("fact")
    suspend fun getDicaDoDia(): Response<DicaDoDia>
}

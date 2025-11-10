package com.example.academia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.academia.data.Exercicio
import com.example.academia.data.Treino
import com.example.academia.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// A classe DicaDoDia foi removida, vamos usar uma String simples
data class TreinoState(
    val treinos: List<Treino> = emptyList(),
    val exerciciosDoTreino: List<Exercicio> = emptyList(),
    val dicaDoDia: String = "" // Agora é apenas uma String
)

class TreinoViewModel(private val repository: AppRepository) : ViewModel() {

    private val _state = MutableStateFlow(TreinoState())
    val state: StateFlow<TreinoState> = _state.asStateFlow()

    // Lista de dicas locais em português
    private val dicasDeTreino = listOf(
        "Mantenha-se hidratado durante todo o dia, não apenas durante o treino.",
        "Aqueça bem antes de começar e alongue-se suavemente no final.",
        "A consistência é mais importante que a intensidade. Treine regularmente.",
        "Durma de 7 a 9 horas por noite para uma boa recuperação muscular.",
        "Varie seus treinos para desafiar diferentes grupos musculares.",
        "Preste atenção na sua alimentação. Proteínas são essenciais para a recuperação.",
        "Não tenha medo de começar com pesos mais leves para aprender a forma correta.",
        "Descanse! Seus músculos crescem e se recuperam nos dias de folga."
    )

    init {
        // Carrega os treinos do banco de dados
        viewModelScope.launch {
            repository.getAllTreinos().collect { treinos ->
                _state.value = _state.value.copy(treinos = treinos)
            }
        }
        // Seleciona uma dica aleatória da lista local
        _state.value = _state.value.copy(dicaDoDia = dicasDeTreino.random())
    }

    // --- Funções CRUD Treino (C, R, U, D) ---
    fun insertTreino(nome: String, data: String, descricao: String) = viewModelScope.launch {
        repository.insertTreino(Treino(nome = nome, data = data, descricao = descricao))
    }
    fun updateTreino(treino: Treino) = viewModelScope.launch {
        repository.updateTreino(treino)
    }
    fun deleteTreino(treino: Treino) = viewModelScope.launch {
        repository.deleteTreino(treino)
    }

    // --- Funções CRUD Exercício (C, R, U, D) ---
    fun loadExercicios(treinoId: Int) = viewModelScope.launch {
        repository.getExerciciosByTreino(treinoId).collect { exercicios ->
            _state.value = _state.value.copy(exerciciosDoTreino = exercicios)
        }
    }
    fun insertExercicio(treinoId: Int, nomeExercicio: String, repeticoes: Int, series: Int) = viewModelScope.launch {
        repository.insertExercicio(Exercicio(treinoFk = treinoId, nomeExercicio = nomeExercicio, repeticoes = repeticoes, series = series))
    }
    fun updateExercicio(exercicio: Exercicio) = viewModelScope.launch {
        repository.updateExercicio(exercicio)
    }
    fun deleteExercicio(exercicio: Exercicio) = viewModelScope.launch {
        repository.deleteExercicio(exercicio)
    }

    // A função da API foi removida

    // Fábrica para injeção de dependência simples
    companion object {
        fun Factory(repository: AppRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TreinoViewModel::class.java)) {
                        return TreinoViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
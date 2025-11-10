package com.example.academia.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.academia.data.Exercicio
import com.example.academia.ui.util.shareText
import com.example.academia.viewmodel.TreinoViewModel

// Adiciona a anotação para APIs experimentais do Compose
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinoDetailScreen(
    viewModel: TreinoViewModel,
    treinoId: Int
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(treinoId) {
        viewModel.loadExercicios(treinoId)
    }

    val exercicios = state.exerciciosDoTreino
    val treinoAtual = state.treinos.find { it.treinoId == treinoId }

    var showAddExercicioDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(treinoAtual?.nome ?: "Detalhes do Treino") },
                actions = {
                    IconButton(onClick = {
                        val na = "N/D"
                        val shareTextContent = buildString {
                            append("Resumo do Treino: ${treinoAtual?.nome ?: na}\n")
                            append("Data: ${treinoAtual?.data ?: na}\n\n")
                            append("Exercícios:\n")
                            exercicios.forEach {
                                append("- ${it.nomeExercicio}: ${it.series} séries de ${it.repeticoes} reps\n")
                            }
                        }
                        shareText(context, shareTextContent)
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Compartilhar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddExercicioDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Exercício")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )) {
            item {
                Text(
                    text = "Descrição: ${treinoAtual?.descricao ?: "Nenhuma descrição"}",
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                Text(
                    text = "Lista de Exercícios:",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }
            items(exercicios) { exercicio ->
                ExercicioItem(
                    exercicio = exercicio,
                    onDelete = { viewModel.deleteExercicio(exercicio) },
                    onUpdate = { nome, reps, series ->
                        // SOLUÇÃO: Criando o objeto Exercicio manualmente para evitar erro de cache do 'copy'
                        val exercicioAtualizado = Exercicio(
                            exercicioId = exercicio.exercicioId, // Mantém o ID original
                            treinoFk = exercicio.treinoFk,       // Mantém a FK original
                            nomeExercicio = nome,               // Novo nome
                            repeticoes = reps,                  // Novas repetições
                            series = series                     // Novas séries
                        )
                        viewModel.updateExercicio(exercicioAtualizado)
                    }
                )
            }
        }
    }

    if (showAddExercicioDialog) {
        AdicionarExercicioDialog(
            onDismiss = { showAddExercicioDialog = false },
            onConfirm = { nome, reps, series ->
                viewModel.insertExercicio(treinoId, nome, reps, series)
                showAddExercicioDialog = false
            }
        )
    }
}
// ... (ExercicioItem, AdicionarExercicioDialog) - Omitido por brevidade, use o código completo da resposta anterior
@Composable
fun ExercicioItem(
    exercicio: Exercicio,
    onDelete: () -> Unit,
    onUpdate: (String, Int, Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = exercicio.nomeExercicio, style = MaterialTheme.typography.titleMedium)
                Text(text = "Séries: ${exercicio.series} | Repetições: ${exercicio.repeticoes}", style = MaterialTheme.typography.bodyMedium)
            }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Mais opções")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar (Update)") },
                        onClick = {
                            expanded = false
                            showEditDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Excluir (Delete)") },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }

    if (showEditDialog) {
        AdicionarExercicioDialog(
            onDismiss = { showEditDialog = false },
            onConfirm = onUpdate,
            initialNome = exercicio.nomeExercicio,
            initialReps = exercicio.repeticoes.toString(),
            initialSeries = exercicio.series.toString(),
            isEditing = true
        )
    }
}

@Composable
fun AdicionarExercicioDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int) -> Unit,
    initialNome: String = "",
    initialReps: String = "",
    initialSeries: String = "",
    isEditing: Boolean = false
) {
    var nome by remember { mutableStateOf(initialNome) }
    var repsText by remember { mutableStateOf(initialReps) }
    var seriesText by remember { mutableStateOf(initialSeries) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Editar Exercício" else "Novo Exercício") },
        text = {
            Column {
                OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do Exercício") })
                OutlinedTextField(value = repsText, onValueChange = { repsText = it }, label = { Text("Repetições") })
                OutlinedTextField(value = seriesText, onValueChange = { seriesText = it }, label = { Text("Séries") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val reps = repsText.toIntOrNull() ?: 0
                    val series = seriesText.toIntOrNull() ?: 0
                    onConfirm(nome, reps, series)
                },
                enabled = nome.isNotBlank() && repsText.toIntOrNull() != null && seriesText.toIntOrNull() != null
            ) {
                Text(if (isEditing) "Salvar Alterações" else "Salvar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

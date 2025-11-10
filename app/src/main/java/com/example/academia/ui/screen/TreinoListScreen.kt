package com.example.academia.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academia.data.Treino
import com.example.academia.viewmodel.TreinoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinoListScreen(
    viewModel: TreinoViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val treinos = state.treinos

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Meu Diário de Treinos") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Treino")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💡 Dica de Treino:", // Título atualizado
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Lógica de loading e erro removida, agora só exibe a dica
                    Text(
                        text = state.dicaDoDia,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
                items(treinos) { treino ->
                    TreinoItem(
                        treino = treino,
                        onClick = { onNavigateToDetail(treino.treinoId) },
                        onDelete = { viewModel.deleteTreino(treino) },
                        onUpdate = { nome, data, descricao ->
                            viewModel.updateTreino(treino.copy(nome = nome, data = data, descricao = descricao))
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AdicionarTreinoDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { nome, data, descricao ->
                viewModel.insertTreino(nome, data, descricao)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TreinoItem(
    treino: Treino,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: (String, String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = treino.nome, style = MaterialTheme.typography.titleLarge)
                Text(text = "Data: ${treino.data}", style = MaterialTheme.typography.bodyMedium)
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
                        text = { Text("Editar") },
                        onClick = {
                            expanded = false
                            showEditDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Excluir") },
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
        AdicionarTreinoDialog(
            onDismiss = { showEditDialog = false },
            onConfirm = onUpdate,
            initialNome = treino.nome,
            initialData = treino.data,
            initialDescricao = treino.descricao,
            isEditing = true
        )
    }
}

@Composable
fun AdicionarTreinoDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
    initialNome: String = "",
    initialData: String = "",
    initialDescricao: String = "",
    isEditing: Boolean = false
) {
    var nome by remember { mutableStateOf(initialNome) }
    var data by remember { mutableStateOf(initialData) }
    var descricao by remember { mutableStateOf(initialDescricao) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Editar Treino" else "Novo Treino") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!isEditing) {
                    Text(
                        text = "Crie um novo treino para registrar seus exercícios. Você poderá editá-lo ou excluí-lo depois.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome do Treino") },
                    placeholder = { Text("Ex: Treino de Peito e Tríceps") }
                )
                OutlinedTextField(
                    value = data,
                    onValueChange = { data = it },
                    label = { Text("Data") },
                    placeholder = { Text("Ex: 25/07/2024") }
                )
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição (Opcional)") },
                    placeholder = { Text("Ex: Foco em hipertrofia") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nome, data, descricao) },
                enabled = nome.isNotBlank() && data.isNotBlank()
            ) {
                Text(if (isEditing) "Salvar" else "Adicionar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

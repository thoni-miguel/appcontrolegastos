package br.ceub.app_controle_gastos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.ceub.app_controle_gastos.model.Category
import br.ceub.app_controle_gastos.ui.viewmodel.CategoryViewModel

@Composable
fun CategoriaScreen(viewModel: CategoryViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val categories by viewModel.categories.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            Text("Categorias", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(categories) { category ->
                    CategoriaItem(category)
                }
            }
        }
    }

    if (showDialog) {
        CategoriaDialog(
            onDismiss = { showDialog = false },
            onSave = { newCategory ->
                viewModel.insertCategory(newCategory)
                showDialog = false
            }
        )
    }
}

@Composable
fun CategoriaItem(category: Category) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = category.color?.let { Color(it) } ?: MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(category.name, style = MaterialTheme.typography.titleMedium)
            if (!category.description.isNullOrBlank()) {
                Text(category.description!!, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@Composable
fun CategoriaDialog(
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit
) {
    val coresDisponiveis = listOf(
        0xFF2196F3.toInt(), // Azul
        0xFF4CAF50.toInt(), // Verde
        0xFFFFEB3B.toInt(), // Amarelo
        0xFFF44336.toInt(), // Vermelho
        0xFF9C27B0.toInt()  // Roxo
    )

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var corSelecionada by remember { mutableStateOf<Int?>(null) }
    var erroNome by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Categoria") },
        text = {
            Column {
                OutlinedTextField(
                    value = nome,
                    onValueChange = {
                        nome = it
                        erroNome = false
                    },
                    label = { Text("Nome") },
                    isError = erroNome,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Cor (opcional):")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    coresDisponiveis.forEach { cor ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(cor))
                                .clickable { corSelecionada = cor }
                                .then(
                                    if (corSelecionada == cor) Modifier.border(2.dp, Color.Black)
                                    else Modifier
                                )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (nome.isBlank()) {
                    erroNome = true
                    return@TextButton
                }
                val novaCategory = Category(
                    name = nome.trim(),
                    description = descricao.trim().ifBlank { null },
                    color = corSelecionada
                )
                onSave(novaCategory)
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
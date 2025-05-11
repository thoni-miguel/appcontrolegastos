package br.ceub.app_controle_gastos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.ceub.app_controle_gastos.model.ShoppingList
import br.ceub.app_controle_gastos.ui.viewmodel.ShoppingListViewModel
import androidx.navigation.NavHostController
import br.ceub.app_controle_gastos.ui.util.autoFocus
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel, navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var editingListId by remember { mutableStateOf<Int?>(null) }
    var name by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<ShoppingList?>(null) }

    val shoppingLists by viewModel.shoppingLists.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Lista de Compras", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(shoppingLists) { list ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("listDetail/${list.id}")
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = list.name, style = MaterialTheme.typography.titleMedium)
                                Row {
                                    IconButton(onClick = {
                                        showDialog = true
                                        name = list.name
                                        editingListId = list.id
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar lista")
                                    }
                                    IconButton(onClick = { listToDelete = list }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Excluir lista")
                                    }
                                }
                            }
                            val formatter =
                                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            Text(
                                text = "Criado em: ${
                                    formatter.format(java.util.Date(list.createdAt))
                                }",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                editingListId = null
            },
            title = {
                Text(if (editingListId != null) "Editar Lista" else "Nova Lista")
            },
            text = {
                Column {
                    val nameMaxLength = 40
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            if (it.length <= nameMaxLength) {
                                name = it
                                showError = false
                            }
                        },
                        label = { Text("Nome") },
                        isError = showError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .autoFocus(),
                        singleLine = true
                    )
                    Text("${name.length} / $nameMaxLength", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (name.isBlank()) {
                        showError = true
                        return@TextButton
                    }
                    if (editingListId != null) {
                        viewModel.updateList(ShoppingList(id = editingListId!!, name = name.trim()))
                    } else {
                        viewModel.insertList(ShoppingList(name = name.trim()))
                    }
                    showDialog = false
                    name = ""
                    editingListId = null
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    editingListId = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    listToDelete?.let { list ->
        AlertDialog(
            onDismissRequest = { listToDelete = null },
            title = { Text("Confirmar exclusão") },
            text = { Text("Tem certeza que deseja excluir a lista \"${list.name}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteList(list)
                    listToDelete = null
                }) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { listToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

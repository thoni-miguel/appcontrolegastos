
package br.ceub.app_controle_gastos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.ceub.app_controle_gastos.model.ShoppingList
import br.ceub.app_controle_gastos.ui.viewmodel.ShoppingListViewModel
import androidx.navigation.NavHostController

@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel, navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }

    val shoppingLists by viewModel.shoppingLists.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Your Shopping Lists", style = MaterialTheme.typography.headlineMedium)

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
                            Text(text = list.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "Created: ${
                                    java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                                        .format(java.util.Date(list.createdAt))
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
        var name by remember { mutableStateOf("") }
        var showError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("New Shopping List") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            showError = false
                        },
                        label = { Text("Name") },
                        isError = showError,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (name.isBlank()) {
                        showError = true
                        return@TextButton
                    }
                    viewModel.insertList(ShoppingList(name = name.trim()))
                    showDialog = false
                    name = ""
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


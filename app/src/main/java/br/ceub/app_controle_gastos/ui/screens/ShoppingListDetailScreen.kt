package br.ceub.app_controle_gastos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.ceub.app_controle_gastos.ui.viewmodel.CategoryViewModel
import br.ceub.app_controle_gastos.ui.viewmodel.ItemViewModel
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailScreen(
    shoppingListId: Int,
    itemViewModel: ItemViewModel,
    categoryViewModel: CategoryViewModel,
    navController: NavHostController
) {
    var showDialog by remember { mutableStateOf(false) }

    val items by itemViewModel.items.collectAsState()

    val categories by categoryViewModel.categories.collectAsState()

    val filteredItems = items.filter { it.shoppingListId == shoppingListId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Items in List") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
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
            Text("Items in this List", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredItems) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "R$ ${item.price}", style = MaterialTheme.typography.bodyMedium)
                            if (!item.description.isNullOrBlank()) {
                                Text(text = item.description, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        ItemDialog(
            categories = categories,
            onDismiss = { showDialog = false },
            onSalvar = {
                itemViewModel.insertItem(it)
                showDialog = false
            },
            shoppingListId = shoppingListId
        )
    }
}

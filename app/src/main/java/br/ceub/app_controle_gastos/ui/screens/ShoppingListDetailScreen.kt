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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.ui.Alignment
import br.ceub.app_controle_gastos.ui.util.formatToBRL
import br.ceub.app_controle_gastos.ui.viewmodel.ShoppingListViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailScreen(
    shoppingListId: Int,
    itemViewModel: ItemViewModel,
    categoryViewModel: CategoryViewModel,
    shoppingListViewModel: ShoppingListViewModel,
    navController: NavHostController
) {
    var showDialog by remember { mutableStateOf(false) }

    val items by itemViewModel.items.collectAsState()

    val categories by categoryViewModel.categories.collectAsState()

    val filteredItems = items.filter { it.shoppingListId == shoppingListId }

    val shoppingList by shoppingListViewModel.getListById(shoppingListId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(shoppingList?.name ?: "Itens da lista") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
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
            val total = filteredItems.sumOf { it.price }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lista de itens",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Total: R$ %.2f".format(Locale("pt", "BR"), total),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredItems) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = String.format(Locale("pt", "BR"), "R$ %.2f", item.price),
                                style = MaterialTheme.typography.bodyMedium
                            )
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

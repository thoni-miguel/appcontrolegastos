package br.ceub.app_controle_gastos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.ceub.app_controle_gastos.model.Category
import br.ceub.app_controle_gastos.model.Item
import br.ceub.app_controle_gastos.ui.viewmodel.ItemViewModel
import br.ceub.app_controle_gastos.data.CategoryDao


@Composable
fun ItemScreen(
    navController: NavController,
    viewModel: ItemViewModel,
    categoryDao: CategoryDao,
) {

    var showDialog by remember { mutableStateOf(false) }

    val items by viewModel.items.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("add")
            }
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Nome da Lista", style = MaterialTheme.typography.headlineMedium)
            //modificar pro texto superior deve mostrar o nome personalizado que o usuário deu para a lista

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(items) { item ->
                    EnumItem(item)
                }
            }
        }
    }

    if (showDialog) {
        val categories by categoryDao.listAll().collectAsState(initial = emptyList())
        ItemDialog(
            categories = categories,
            onDismiss = { showDialog = false },
            onSalvar = { newItem ->
                viewModel.insertItem(newItem)
                showDialog = false
            },
            shoppingListId = 1
        )
    }
}

@Composable
fun EnumItem(item: Item) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            item.name, style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "R$ %.2f".format(item.price), style = MaterialTheme.typography.titleMedium
        )
    }
}


@Composable
fun ItemDialog(
    categories: List<Category>,
    onDismiss: () -> Unit,
    onSalvar: (Item) -> Unit,
    shoppingListId: Int
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var errorName by remember { mutableStateOf(false) }
    var errorPrice by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableStateOf<Int?>(1) } // default: "Nenhum"

    AlertDialog(onDismissRequest = onDismiss, title = { Text("New Item") }, text = {
        Column {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    errorName = false
                },
                label = { Text("Name") },
                isError = errorName,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                    errorPrice = false
                },
                label = { Text("Price") },
                isError = errorPrice,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategoryDropdownMenu(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = { categoria ->
                    selectedCategoryId = categoria.id
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }, confirmButton = {
        TextButton(onClick = {
            if (name.isBlank()) {
                errorName = true
                return@TextButton
            }
            if (price.isBlank()) {
                errorPrice = true
                return@TextButton
            }
            val newItem = Item(
                name = name.trim(),
                price = price.trim().toDouble(),
                categoryId = selectedCategoryId ?: 1,
                shoppingListId = shoppingListId,
                description = description.trim().ifBlank { null }
            )
            onSalvar(newItem)
        }) {
            Text("Save")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

@Composable
fun CategoryDropdownMenu(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelected: (Category) -> Unit,
) {
    val selectedCategory = categories.find { it.id == selectedCategoryId }
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "Choose category",
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        Modifier.clickable { expanded = true })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true })

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { categoria ->
                    val cor = try {
                        categoria.color?.let { Color(it) } ?: Color.Gray
                    } catch (e: Exception) {
                        Color.Gray
                    }
                    DropdownMenuItem(text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(cor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(categoria.name)
                        }
                    }, onClick = {
                        onCategorySelected(categoria)
                        expanded = false
                    })
                }
            }
        }
    }
}

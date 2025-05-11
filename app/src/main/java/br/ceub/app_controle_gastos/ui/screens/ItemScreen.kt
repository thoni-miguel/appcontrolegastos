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
import br.ceub.app_controle_gastos.ui.util.autoFocus
import br.ceub.app_controle_gastos.ui.util.formatToBRL

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
                label = { Text("Nome") },
                isError = errorName,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .autoFocus()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = formatToBRL(price),
                onValueChange = {
                    if (it.length <= 9) {
                        price = it.replace(Regex("[^\\d]"), "")
                        errorPrice = false
                    }
                },
                label = { Text("Preço") },
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
                label = { Text("Descrição (opcional)") },
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
                price = try {
                    price.toDouble() / 100
                } catch (e: NumberFormatException) {
                    errorPrice = true
                    return@TextButton
                },
                categoryId = selectedCategoryId ?: 1,
                shoppingListId = shoppingListId,
                description = description.trim().ifBlank { null }
            )
            onSalvar(newItem)
        }) {
            Text("Salvar")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancelar")
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

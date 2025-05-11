package br.ceub.app_controle_gastos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.ceub.app_controle_gastos.data.ShoppingListDao
import br.ceub.app_controle_gastos.model.ShoppingList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingListViewModel(private val dao: ShoppingListDao) : ViewModel() {

    val shoppingLists: StateFlow<List<ShoppingList>> =
        dao.listAll().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun insertList(list: ShoppingList) {
        viewModelScope.launch {
            dao.insert(list)
        }
    }

    fun updateList(list: ShoppingList) {
        viewModelScope.launch {
            dao.update(list)
        }
    }

    fun deleteList(list: ShoppingList) {
        viewModelScope.launch {
            dao.delete(list)
        }
    }

    fun getListById(listId: Int): Flow<ShoppingList> = dao.getById(listId)

    class Factory(private val dao: ShoppingListDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShoppingListViewModel(dao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
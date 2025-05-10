package br.ceub.app_controle_gastos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.ceub.app_controle_gastos.data.ItemDao
import br.ceub.app_controle_gastos.model.Item
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemViewModel(private val dao: ItemDao): ViewModel() {

    val items: StateFlow<List<Item>> = dao.listAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = emptyList()
    )

    fun insertItem(item: Item) {
        viewModelScope.launch {
            dao.insert(item)
        }
    }

    class Factory(private val dao: ItemDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ItemViewModel(dao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}
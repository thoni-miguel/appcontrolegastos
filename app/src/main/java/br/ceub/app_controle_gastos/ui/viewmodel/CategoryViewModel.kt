package br.ceub.app_controle_gastos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.ceub.app_controle_gastos.model.Category
import br.ceub.app_controle_gastos.data.CategoryDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(private val dao: CategoryDao) : ViewModel() {

    val categories: StateFlow<List<Category>> = dao.listAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertCategory(category: Category) {
        viewModelScope.launch {
            dao.insert(category)
        }
    }

    class Factory(private val dao: CategoryDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CategoryViewModel(dao) as T
            }
            throw IllegalArgumentException("ViewModel desconhecida")
        }
    }
}
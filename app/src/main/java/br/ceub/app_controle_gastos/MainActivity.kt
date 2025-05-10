package br.ceub.app_controle_gastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import br.ceub.app_controle_gastos.data.AppDatabase
import br.ceub.app_controle_gastos.ui.navigation.BottomNavigationBar
import br.ceub.app_controle_gastos.ui.viewmodel.CategoryViewModel
import br.ceub.app_controle_gastos.ui.navigation.MainNavigation
import br.ceub.app_controle_gastos.ui.viewmodel.ShoppingListViewModel
import br.ceub.app_controle_gastos.ui.theme.AppcontrolegastosTheme
import br.ceub.app_controle_gastos.ui.viewmodel.ItemViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppcontrolegastosTheme {
                val db = AppDatabase.getDatabase(applicationContext)
                val shoppingListDao = db.shoppingListDao()
                val categoryDao = db.categoryDao()
                val itemDao = db.itemDao()

                val shoppingListViewModel: ShoppingListViewModel =
                    viewModel(factory = ShoppingListViewModel.Factory(shoppingListDao))
                val categoryViewModel: CategoryViewModel =
                    viewModel(factory = CategoryViewModel.Factory(categoryDao))
                val itemViewModel: ItemViewModel = viewModel(factory = ItemViewModel.Factory(itemDao))
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) { innerPadding ->
                    MainNavigation(
                        navController = navController,
                        shoppingListViewModel = shoppingListViewModel,
                        categoryViewModel = categoryViewModel,
                        itemViewModel = itemViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppcontrolegastosTheme {
        // Preview content can be adjusted or removed as needed
    }
}
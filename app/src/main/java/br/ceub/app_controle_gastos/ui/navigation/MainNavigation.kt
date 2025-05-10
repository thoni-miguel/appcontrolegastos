package br.ceub.app_controle_gastos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.ceub.app_controle_gastos.ui.viewmodel.CategoryViewModel
import br.ceub.app_controle_gastos.ui.viewmodel.ShoppingListViewModel
import br.ceub.app_controle_gastos.ui.viewmodel.ItemViewModel
import br.ceub.app_controle_gastos.ui.screens.CategoriaScreen
import br.ceub.app_controle_gastos.ui.screens.ShoppingListScreen

import androidx.navigation.NavType
import androidx.navigation.navArgument
import br.ceub.app_controle_gastos.ui.screens.ShoppingListDetailScreen

@Composable
fun MainNavigation(
    navController: NavHostController,
    shoppingListViewModel: ShoppingListViewModel,
    categoryViewModel: CategoryViewModel,
    itemViewModel: ItemViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "lists",
        modifier = modifier
    ) {
        composable("lists") {
            ShoppingListScreen(viewModel = shoppingListViewModel, navController = navController)
        }
        composable("categories") {
            CategoriaScreen(viewModel = categoryViewModel)
        }
        composable(
            route = "listDetail/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId") ?: return@composable
            ShoppingListDetailScreen(
                shoppingListId = listId,
                itemViewModel = itemViewModel,
                navController = navController,
                categoryViewModel = categoryViewModel
            )
        }
    }
}

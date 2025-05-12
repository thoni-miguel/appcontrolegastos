package br.ceub.app_controle_gastos

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.ceub.app_controle_gastos.data.AppDatabase
import br.ceub.app_controle_gastos.ui.navigation.BottomNavigationBar
import br.ceub.app_controle_gastos.ui.navigation.MainNavigation
import br.ceub.app_controle_gastos.ui.theme.AppcontrolegastosTheme
import br.ceub.app_controle_gastos.ui.viewmodel.CategoryViewModel
import br.ceub.app_controle_gastos.ui.viewmodel.ItemViewModel
import br.ceub.app_controle_gastos.ui.viewmodel.ShoppingListViewModel
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
                val itemViewModel: ItemViewModel =
                    viewModel(factory = ItemViewModel.Factory(itemDao))
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
        scheduleDailyNotification()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleDailyNotification() {
        val delay = calculateInitialDelayUntil18h()

        val request = PeriodicWorkRequestBuilder<br.ceub.app_controle_gastos.ui.NotificationWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "daily_gastos_notification",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateInitialDelayUntil18h(): Long {
        val now = java.time.LocalDateTime.now()
        val target = now.withHour(18).withMinute(0).withSecond(0).withNano(0)

        // Verifica se já passou do horário target (18h)
        // Se sim, adiciona um dia para a próxima notificação
        val delay = if (now.isAfter(target)) {
            java.time.Duration.between(now, target.plusDays(1))
        } else {
            java.time.Duration.between(now, target)
        }
        return delay.toMillis()
    }
}
package com.example.academia

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.academia.data.AppDatabase
import com.example.academia.network.RetrofitClient
import com.example.academia.repository.AppRepository
import com.example.academia.ui.screen.TreinoDetailScreen
import com.example.academia.ui.screen.TreinoListScreen
import com.example.academia.viewmodel.TreinoViewModel

object Destinations {
    const val TREINO_LIST_ROUTE = "treino_list"
    const val TREINO_DETAIL_ROUTE = "treino_detail/{treinoId}"

    fun getTreinoDetailRoute(treinoId: Int): String {
        return "treino_detail/$treinoId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Inicialização do Repositório e ViewModel
    val database = AppDatabase.getDatabase(context)
    val repository = AppRepository(database.treinoDao(), RetrofitClient.apiService)
    val viewModel: TreinoViewModel = viewModel(
        factory = TreinoViewModel.Factory(repository)
    )

    NavHost(navController = navController, startDestination = Destinations.TREINO_LIST_ROUTE) {
        composable(Destinations.TREINO_LIST_ROUTE) {
            TreinoListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { treinoId ->
                    navController.navigate(Destinations.getTreinoDetailRoute(treinoId))
                }
            )
        }
        composable(
            route = Destinations.TREINO_DETAIL_ROUTE,
            arguments = listOf(navArgument("treinoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val treinoId = backStackEntry.arguments?.getInt("treinoId") ?: 0
            TreinoDetailScreen(viewModel = viewModel, treinoId = treinoId)
        }
    }
}
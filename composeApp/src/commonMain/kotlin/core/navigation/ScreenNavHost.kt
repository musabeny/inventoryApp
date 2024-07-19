package core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cashflow.presentation.CashFlowScreen
import core.util.PRODUCT_ID
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import report.presentation.ReportScreen
import sales.presentation.SalesScreen
import settings.presentation.inventory.InventoryScreen
import settings.presentation.inventory.InventoryState
import settings.presentation.inventory.InventoryViewModel
import settings.presentation.product.ProductScreen
import settings.presentation.product.ProductViewModel
import settings.presentation.setting.SettingScreen
import settings.presentation.setting.SettingViewModel

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ScreenNavHost(
    navHostController: NavHostController,
    snackBarHost: SnackbarHostState,
    inventoryViewModel: InventoryViewModel,
    inventoryState: InventoryState
){

    NavHost(navController = navHostController, startDestination = Routes.Inventory.route){
        composable(route = Routes.Report.route){
            ReportScreen()
        }
        composable(route = Routes.Sales.route){
            SalesScreen()
        }

        composable(route = Routes.CashFlow.route){
            CashFlowScreen()
        }
        composable(route = Routes.Settings.route){
            val viewModel = koinViewModel<SettingViewModel>()
            val state = viewModel.state.collectAsState().value
            SettingScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navController = navHostController
            )
        }
        composable(route = Routes.Inventory.route){
//            val viewModel = koinViewModel<InventoryViewModel>()
//            val state = inventoryViewModel.state.collectAsState().value
            InventoryScreen(
                state = inventoryState,
                onEvent = inventoryViewModel::onEvent,
                navController = navHostController,

            )
        }

        composable(
            route = Routes.Product.route,
            arguments = listOf(
                navArgument(PRODUCT_ID){type = NavType.LongType}
            )
        ){
            val productId = it.arguments?.getLong(PRODUCT_ID) ?: 0
            val viewModel = koinViewModel<ProductViewModel>()
            val state = viewModel.state.collectAsState().value
            val uiEvent = viewModel.uiEvent
            ProductScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navController = navHostController,
                snackBarHost = snackBarHost,
                uiEvent = uiEvent,
                productId = productId
            )
        }
    }
}

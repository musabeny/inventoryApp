package core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cashflow.presentation.cashFlow.CashFlowScreen
import cashflow.presentation.cashFlow.CashFlowViewModel
import cashflow.presentation.customCalender.CalenderScreen
import cashflow.presentation.customCalender.CalenderViewModel
import core.util.PRODUCT_ID
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import report.presentation.ReportScreen
import sales.presentation.SalesScreen
import settings.presentation.inventory.InventoryScreen
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
    updateInventoryState:(Boolean,String)->Unit,

){

    NavHost(navController = navHostController, startDestination = Routes.CashFlow.route){
        composable(route = Routes.Report.route){
            ReportScreen()
        }
        composable(route = Routes.Sales.route){
            SalesScreen()
        }

        composable(route = Routes.CashFlow.route){
            val viewModel = koinViewModel<CashFlowViewModel>()
            val  state = viewModel.state.collectAsState().value

            CashFlowScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navController = navHostController
            )
        }

        composable(route = Routes.Calender.route){
            val viewModel = koinViewModel<CalenderViewModel>()
            val state = viewModel.state.collectAsState().value
            CalenderScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navController = navHostController
            )
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
            val state = inventoryViewModel.state.collectAsState().value
            updateInventoryState(state.showSearchIcon,state.searchText)
            val uiEvent = inventoryViewModel.uiEvent
            InventoryScreen(
                state = state,
                onEvent = inventoryViewModel::onEvent,
                navController = navHostController,
                uiEvent = uiEvent,
                snackBarHost =snackBarHost
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

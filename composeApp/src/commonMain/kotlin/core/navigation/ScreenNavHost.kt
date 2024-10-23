package core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cashflow.presentation.breakDown.BreakDownScreen
import cashflow.presentation.breakDown.BreakDownViewModel
import cashflow.presentation.cashFlow.CashFlowEvent
import cashflow.presentation.cashFlow.CashFlowScreen
import cashflow.presentation.cashFlow.CashFlowViewModel
import cashflow.presentation.customCalender.CalenderScreen
import cashflow.presentation.customCalender.CalenderViewModel
import cashflow.presentation.purchase.PurchaseScreen
import cashflow.presentation.purchase.PurchaseViewModel
import cashflow.presentation.purchaseReceipt.PurchaseReceiptScreen
import cashflow.presentation.purchaseReceipt.PurchaseReceiptViewModel
import core.util.BILL_ID
import core.util.CATEGORY_ID
import core.util.IS_INCOME_OR_EXPENSE
import core.util.PRODUCT_ID
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import report.presentation.ReportScreen
import sales.presentation.SalesScreen
import settings.presentation.inventory.InventoryEvent
import settings.presentation.inventory.InventoryScreen
import settings.presentation.inventory.InventoryViewModel
import settings.presentation.product.ProductScreen
import settings.presentation.product.ProductViewModel
import settings.presentation.setting.SettingEvent
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
            val inventoryState = inventoryViewModel.state.collectAsState().value
            CashFlowScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navController = navHostController,
                inventoryEvent = inventoryViewModel::onEvent,
                inventoryState = inventoryState,
                snackBarHost = snackBarHost,
                uiEvent = viewModel.uiEvent
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

        composable(
            route = Routes.BreakDown.route,
            arguments = listOf(
                navArgument(CATEGORY_ID){type = NavType.LongType},
                navArgument(IS_INCOME_OR_EXPENSE){type = NavType.IntType}
            )
        ){
          val categoryId = it.arguments?.getLong(CATEGORY_ID)
          val incomeOrExpense = it.arguments?.getInt(IS_INCOME_OR_EXPENSE)
          val viewModel = koinViewModel<BreakDownViewModel>()
          val state = viewModel.state.value

            val viewModelCashFlow = koinViewModel<CashFlowViewModel>()
            val  stateCashFlow = viewModelCashFlow.state.collectAsState().value

          BreakDownScreen(
              state = state,
              onEvent = viewModel::onEvent,
              categoryId = categoryId,
              isIncomeOrExpense = incomeOrExpense,
              stateCashFlow = stateCashFlow,
              onEventCashFlow = viewModelCashFlow::onEvent,
              navController = navHostController
          )
        }


        composable(
            route = Routes.Purchase.route,
            arguments = listOf(
                navArgument(BILL_ID){type = NavType.StringType}
            )
        ){
            val viewModel = koinViewModel<PurchaseViewModel>()
            val  state = viewModel.state.collectAsState().value
            val billId = it.arguments?.getString(BILL_ID)?.toLongOrNull()
            PurchaseScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navController = navHostController,
                snackBarHost = snackBarHost,
                uiEvent = viewModel.uiEvent,
                billId = billId
            )
        }

        composable(
            route = Routes.PurchaseReceipt.route,
            arguments = listOf(
                navArgument(BILL_ID){type = NavType.StringType}
            )
        ){
            val billId = it.arguments?.getString(BILL_ID)?.toLongOrNull()
            val viewModel = koinViewModel<PurchaseReceiptViewModel>()
            val state = viewModel.state.collectAsState().value

            PurchaseReceiptScreen(
                state = state,
                onEvent = viewModel::onEvent,
                snackBarHost = snackBarHost,
                uiEvent = viewModel.uiEvent,
                navController = navHostController,
                billId = billId
            )

        }
    }
}

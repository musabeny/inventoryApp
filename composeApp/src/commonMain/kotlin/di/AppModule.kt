package di

import cashflow.presentation.breakDown.BreakDownViewModel
import cashflow.presentation.cashFlow.CashFlowViewModel
import cashflow.presentation.customCalender.CalenderViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import settings.presentation.inventory.InventoryViewModel
import settings.presentation.product.ProductViewModel
import settings.presentation.setting.SettingViewModel

val appModule = module {
    single { "Hello word" }
    viewModelOf(::SettingViewModel)
    viewModelOf(::InventoryViewModel)
    viewModelOf(::ProductViewModel)
    viewModelOf(::CashFlowViewModel)
    viewModelOf(::CalenderViewModel)
    viewModelOf(::BreakDownViewModel)
}
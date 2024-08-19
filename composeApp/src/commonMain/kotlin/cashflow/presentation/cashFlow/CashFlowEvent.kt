package cashflow.presentation.cashFlow

import androidx.navigation.NavController
import kotlinx.datetime.LocalDate
import settings.domain.model.category.CategoryWithColor

sealed interface CashFlowEvent {
    data class GoToDateSelection(val navController: NavController):CashFlowEvent
    data object ThisWeek:CashFlowEvent
    data object Next:CashFlowEvent
    data object Back:CashFlowEvent
    data class SelectedDateRange(val dateRange:ClosedRange<LocalDate>):CashFlowEvent
    data object Categories:CashFlowEvent
    data class ShowIncomeForm(val show:Boolean):CashFlowEvent
    data class ShowCategoryDropDown(val show: Boolean):CashFlowEvent
    data class SelectedCategory(val selected:CategoryWithColor):CashFlowEvent
}
package cashflow.presentation.cashFlow

import androidx.navigation.NavController
import kotlinx.datetime.LocalDate

sealed interface CashFlowEvent {
    data class GoToDateSelection(val navController: NavController):CashFlowEvent
    data object ThisWeek:CashFlowEvent
    data object Next:CashFlowEvent
    data object Back:CashFlowEvent
    data class SelectedDateRange(val dateRange:ClosedRange<LocalDate>):CashFlowEvent
}
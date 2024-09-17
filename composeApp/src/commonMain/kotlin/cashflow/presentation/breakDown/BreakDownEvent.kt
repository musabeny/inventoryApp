package cashflow.presentation.breakDown

import androidx.navigation.NavController
import cashflow.presentation.cashFlow.CashFlowEvent

sealed interface BreakDownEvent {

    data class GetIncomeOrExpense(val categoryId:Long?, val incomeOrExpense:Int?, val onEventCashFlow:(CashFlowEvent) -> Unit):BreakDownEvent
    data class CloseScreen(val navController: NavController):BreakDownEvent
}
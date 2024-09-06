package cashflow.presentation.cashFlow

import androidx.navigation.NavController
import cashflow.domain.enums.IncomeExpenseType
import cashflow.domain.enums.ListViewType
import cashflow.domain.enums.UserFilterType
import cashflow.domain.model.FilterType
import kotlinx.datetime.LocalDate
import settings.domain.model.category.CategoryWithColor

sealed interface CashFlowEvent {
    data class GoToDateSelection(val navController: NavController):CashFlowEvent
    data object ThisWeek:CashFlowEvent
    data object Next:CashFlowEvent
    data object Back:CashFlowEvent
    data class SelectedDateRange(val dateRange:ClosedRange<LocalDate>):CashFlowEvent
    data object Categories:CashFlowEvent
    data class ShowIncomeExpenseForm(val show:Boolean, val incomeExpenseType: IncomeExpenseType):CashFlowEvent
    data class ShowCategoryDropDown(val show: Boolean):CashFlowEvent
    data class SelectedCategory(val selected:CategoryWithColor):CashFlowEvent
    data class EnterAmount(val amount:String?):CashFlowEvent
    data class EnterNote(val note:String?):CashFlowEvent
    data object ToDaysDate:CashFlowEvent
    data object SaveIncomeOrExpense:CashFlowEvent
    data object GetIncomeExpense:CashFlowEvent
    data class ChangeViewType(val view:ListViewType):CashFlowEvent
    data class ShowFilterSheet(val show: Boolean):CashFlowEvent
    data object EntryTypes:CashFlowEvent
    data object IncomeCategory:CashFlowEvent
    data object ExpenseCategory:CashFlowEvent
    data class SelectedFilter(val filterType: FilterType, val isChecked:Boolean):CashFlowEvent
    data class SelectedFilterType(val filterType: UserFilterType):CashFlowEvent
    data object ClearAllFilter:CashFlowEvent
}
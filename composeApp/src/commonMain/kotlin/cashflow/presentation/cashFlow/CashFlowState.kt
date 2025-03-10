package cashflow.presentation.cashFlow

import cashflow.domain.enums.CashFlowTabs
import cashflow.domain.enums.IncomeExpenseType
import cashflow.domain.enums.ListViewType
import cashflow.domain.enums.UserFilterType
import cashflow.domain.model.FilterType
import cashflow.domain.model.IncomeExpense
import cashflow.domain.model.purchase.PurchaseDetail
import cashflow.domain.model.purchase.PurchaseGroupByItem
import kotlinx.datetime.LocalDate
import settings.domain.model.category.CategoryWithColor

data class CashFlowState(
    val dateRange:ClosedRange<LocalDate>? = null,
    val showNextArrow:Boolean = true,
    val categories:List<CategoryWithColor> = emptyList(),
    val showIncomeOrExpenseForm:Boolean = false,
    val showCategoryDropDown:Boolean = false,
    val selectedCategory:CategoryWithColor? = null,
    val amount:String? = null,
    val note:String? = null,
    val today: String = "",
    val incomeExpenseType: IncomeExpenseType = IncomeExpenseType.INCOME,
    val incomeExpenses:Map<LocalDate, List<IncomeExpense>> = mapOf(),
    val viewType:ListViewType = ListViewType.LIST,
    val totalIncome:String = "",
    val totalExpense:String = "",
    val incomeExpensesGroup:Map<Pair<Int, CategoryWithColor>, List<IncomeExpense>> = mapOf(),
    val showFilterSheet:Boolean = false,
    val entryType:List<FilterType> = emptyList(),
    val incomeCategory:List<FilterType> = emptyList(),
    val expenseCategory:List<FilterType> = emptyList(),
    val userFilterType:UserFilterType = UserFilterType.ENTRY,
    val showDeleteDialog:Boolean = false,
    val selectedIncomeExpense:IncomeExpense? = null,
    val purchaseByDate:Map<LocalDate, List<PurchaseDetail>> = mapOf(),
    val purchaseTotal:String = "",
    val purchaseGroupedByItem:  Map<String, List<PurchaseGroupByItem>> = mapOf(),
    val selectedTabIndex:CashFlowTabs = CashFlowTabs.INCOME,
    val members:List<FilterType> = emptyList(),
    val status:List<FilterType> = emptyList(),
    val searchText:String = ""
)

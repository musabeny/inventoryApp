package cashflow.presentation.breakDown

import cashflow.domain.model.IncomeExpense
import kotlinx.datetime.LocalDate
import settings.domain.model.category.CategoryWithColor

data class BreakDownState(
    val incomeExpenses:Map<LocalDate, List<IncomeExpense>> = mapOf(),
    val amount:Double = 0.0,
    val category:CategoryWithColor? = null

)

package cashflow.presentation.cashFlow

import kotlinx.datetime.LocalDate
import settings.domain.model.category.Category
import settings.domain.model.category.CategoryWithColor

data class CashFlowState(
    val dateRange:ClosedRange<LocalDate>? = null,
    val showNextArrow:Boolean = true,
    val categories:List<CategoryWithColor> = emptyList(),
    val showIncomeForm:Boolean = false,
    val showCategoryDropDown:Boolean = false,
    val selectedCategory:CategoryWithColor? = null
)

package cashflow.presentation.cashFlow

import kotlinx.datetime.LocalDate

data class CashFlowState(
    val dateRange:ClosedRange<LocalDate>? = null,
    val showNextArrow:Boolean = true
)

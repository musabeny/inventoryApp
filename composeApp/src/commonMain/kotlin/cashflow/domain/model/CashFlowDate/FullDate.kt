package cashflow.domain.model.CashFlowDate

import kotlinx.datetime.LocalDate

data class FullDate(
    val day: LocalDate? = null,
    var isSelected:Boolean = false,
    val isCurrentDate:Boolean = false,
    val isActive:Boolean = false
)
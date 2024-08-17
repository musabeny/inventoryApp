package cashflow.domain.usecase

import cashflow.domain.usecase.cashFlowCases.DateRange
import cashflow.domain.usecase.cashFlowCases.DaysOfWeek
import cashflow.domain.usecase.cashFlowCases.DaysOfMonth


data class CashFlowUseCases(
    val daysOfWeek: DaysOfWeek = DaysOfWeek(),
    val dayOfMonth: DaysOfMonth = DaysOfMonth(),
    val dateRange: DateRange = DateRange()
)

package cashflow.domain.usecase

import cashflow.domain.usecase.cashFlowCases.DateRange
import cashflow.domain.usecase.cashFlowCases.DaysOfWeek
import cashflow.domain.usecase.cashFlowCases.DaysOfMonth
import cashflow.domain.usecase.cashFlowCases.ValidateAmount


data class CashFlowUseCases(
    val daysOfWeek: DaysOfWeek = DaysOfWeek(),
    val dayOfMonth: DaysOfMonth = DaysOfMonth(),
    val dateRange: DateRange = DateRange(),
    val validateAmount:ValidateAmount = ValidateAmount()
)

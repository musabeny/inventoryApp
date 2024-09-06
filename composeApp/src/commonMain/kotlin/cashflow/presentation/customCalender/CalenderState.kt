package cashflow.presentation.customCalender

import cashflow.domain.model.CashFlowDate.CustomDate
import cashflow.domain.model.CashFlowDate.FullDate
import kotlinx.datetime.LocalDate

data class CalenderState(
    val calender:List<CustomDate> = emptyList(),
    val daysOfWeeks:List<String> = emptyList(),
    val selectedDate: FullDate? = null,
    val dateRange:ClosedRange<LocalDate>? = null,
    val dateSelectedCount:Int = 0,
    val currentMonth:String = "",
    val currentYear:String = ""
)

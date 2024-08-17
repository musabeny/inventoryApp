package cashflow.domain.usecase.cashFlowCases

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class DateRange {
    operator fun invoke(localDate: LocalDate):ClosedRange<LocalDate>{
     return   when(localDate.dayOfWeek){
            DayOfWeek.MONDAY -> localDate..localDate
            DayOfWeek.TUESDAY -> localDate.minus(DatePeriod(days = 1))..localDate
            DayOfWeek.WEDNESDAY -> localDate.minus(DatePeriod(days = 2))..localDate
            DayOfWeek.THURSDAY -> localDate.minus(DatePeriod(days = 3))..localDate
            DayOfWeek.FRIDAY -> localDate.minus(DatePeriod(days = 4))..localDate
            DayOfWeek.SATURDAY -> localDate.minus(DatePeriod(days = 5))..localDate
            DayOfWeek.SUNDAY -> localDate.minus(DatePeriod(days = 6))..localDate
            else -> localDate.minus(DatePeriod(days = 6))..localDate
        }
    }
}
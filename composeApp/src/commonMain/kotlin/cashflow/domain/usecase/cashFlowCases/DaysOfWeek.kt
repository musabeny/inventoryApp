package cashflow.domain.usecase.cashFlowCases

import kotlinx.datetime.DayOfWeek

class DaysOfWeek {
    operator fun invoke():Array<String>{
        val daysOfWeek = Array(7) { "" }
        DayOfWeek.entries.forEachIndexed {index, day ->
           daysOfWeek[index]= abvOfDays(day)
        }
        return daysOfWeek
    }

    private fun abvOfDays(day: DayOfWeek):String{
       return when(day){
            DayOfWeek.MONDAY -> "Mon"
            DayOfWeek.TUESDAY -> "Tue"
            DayOfWeek.WEDNESDAY -> "Wed"
            DayOfWeek.THURSDAY -> "Thi"
            DayOfWeek.FRIDAY -> "Fri"
            DayOfWeek.SATURDAY -> "Sut"
            DayOfWeek.SUNDAY -> "Sun"
            else -> ""
        }
    }
}
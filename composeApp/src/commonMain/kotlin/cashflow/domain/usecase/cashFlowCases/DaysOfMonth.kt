package cashflow.domain.usecase.cashFlowCases

import cashflow.domain.model.CashFlowDate.CustomDate
import cashflow.domain.model.CashFlowDate.FullDate
import cashflow.domain.model.CashFlowDate.YearMonth
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn


class DaysOfMonth {
    operator fun invoke(localDate: LocalDate): List<CustomDate>{
        val toDaysDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val calenders = ArrayList<CustomDate>()
      for (i in 1 .. localDate.monthNumber){

       val date = localDate.minus(DatePeriod(months =(localDate.monthNumber-i)))
          val fullDates =   monthDays(localDate =  date).map {
           FullDate(
               day = it,
               isSelected =  it == toDaysDate,
               isCurrentDate = it == toDaysDate,
               isActive = it != null && it <= toDaysDate
           )
       }

       val result = CustomDate(fullDates)
        calenders.add(result)
      }
    return calenders.toList()
    }

    private fun monthDays(localDate: LocalDate):List<LocalDate?>{
        val currentYearMonth = YearMonth(localDate.year, localDate.month)
        val days = generateSequence(1) {
            it + 1
        }
            .takeWhile { it <= currentYearMonth.atEndOfMonth().dayOfMonth }
            .toList()
        val dateWithDays = ArrayList<LocalDate?>()
        val dayOfWeek = LocalDate(currentYearMonth.year,currentYearMonth.month,days[0]).dayOfWeek

        when(dayOfWeek){
             DayOfWeek.MONDAY ->{
                 days.map { date ->
                dateWithDays.add(LocalDate(localDate.year,localDate.month,date))
                 }
            }
            DayOfWeek.TUESDAY ->{
                dateWithDays.add(null)
              days.map {dateWithDays.add(LocalDate(localDate.year,localDate.month,it)) }
        }
            DayOfWeek.WEDNESDAY ->{
                dateWithDays.add(null)
                dateWithDays.add(null)
                days.map { dateWithDays.add(LocalDate(localDate.year,localDate.month,it))
                }

            }
            DayOfWeek.THURSDAY ->{
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                days.map { dateWithDays.add(LocalDate(localDate.year,localDate.month,it))
                }


            }
            DayOfWeek.FRIDAY ->{
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                days.map { dateWithDays.add(LocalDate(localDate.year,localDate.month,it))
                }

            }
            DayOfWeek.SATURDAY ->{
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                days.map { dateWithDays.add(LocalDate(localDate.year,localDate.month,it))
                }

            }
            DayOfWeek.SUNDAY ->{
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                dateWithDays.add(null)
                days.map { dateWithDays.add(LocalDate(localDate.year,localDate.month,it))
                }

            }
            else -> days.map { dateWithDays.add(LocalDate(localDate.year,localDate.month,it)) }}
        return dateWithDays.toList()
    }
}
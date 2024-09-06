package cashflow.presentation.customCalender

import androidx.navigation.NavController
import cashflow.domain.model.CashFlowDate.FullDate

sealed interface CalenderEvent {
    data object GetCalenderData:CalenderEvent
    data object GetDaysOfWeek:CalenderEvent
    data class SelectDate(val selectedDate: FullDate, val index:Int):CalenderEvent
    data class DateSelected(val navController: NavController):CalenderEvent
    data class ToDay(val navController: NavController):CalenderEvent
    data class Yesterday(val navController: NavController):CalenderEvent
    data class ThisWeek(val navController: NavController):CalenderEvent
    data class LastWeek(val navController: NavController):CalenderEvent
    data class ThisMonth(val navController: NavController):CalenderEvent
    data class ThisYear(val navController: NavController):CalenderEvent
    data object CurrentMonth:CalenderEvent
    data object CurrentYear:CalenderEvent

}
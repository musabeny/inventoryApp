package cashflow.presentation.customCalender

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import cashflow.domain.mapper.isAfter
import cashflow.domain.mapper.iterator
import cashflow.domain.model.CustomDate
import cashflow.domain.model.FullDate
import cashflow.domain.usecase.CashFlowUseCases
import core.util.DATE_RANGE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

class CalenderViewModel(
   private val useCases: CashFlowUseCases
):ViewModel() {
  private val _state = MutableStateFlow(CalenderState())
  val state = _state.asStateFlow()
    private val now = Clock.System.todayIn(TimeZone.currentSystemDefault())
    private var dateSelectedCount = 0
    private val dateRangeList = ArrayList<LocalDate>()
    init {
        onEvent(CalenderEvent.GetCalenderData)
        onEvent(CalenderEvent.GetDaysOfWeek)
        onEvent(CalenderEvent.CurrentMonth)
        onEvent(CalenderEvent.CurrentYear)
    }

   fun onEvent(event: CalenderEvent){
           when(event){
              is CalenderEvent.GetCalenderData ->{

                  val result =  useCases.dayOfMonth(now)
                  _state.update {
                      it.copy(calender = result)
                  }
               }
              is CalenderEvent.GetDaysOfWeek -> {
                  _state.update {
                      it.copy(daysOfWeeks = useCases.daysOfWeek().toList())
                  }
               }
              is CalenderEvent.SelectDate -> {
                   selectDate(event.selectedDate)
               }
              is CalenderEvent.LastWeek -> {
                  val lastWeek =useCases.dateRange(now).start.minus(DatePeriod(days = 7)).. useCases.dateRange(now).start.minus(DatePeriod(days = 1))
                  _state.update {
                      it.copy(dateRange = lastWeek)
                  }
                  _state.value.dateRange?.let {
                      navigateBackWithDateRange(event.navController,it)
                  }
              }
               is CalenderEvent.ThisMonth -> {
                   val thisMonth = now.minus(DatePeriod(days = now.dayOfMonth-1)).. now
                   _state.update {
                       it.copy(dateRange = thisMonth)
                   }
                   _state.value.dateRange?.let {
                       navigateBackWithDateRange(event.navController,it)
                   }
               }
               is CalenderEvent.ThisWeek -> {
                   _state.update {
                       it.copy(dateRange = useCases.dateRange(now))
                   }
                   _state.value.dateRange?.let {
                       navigateBackWithDateRange(event.navController,it)
                   }
               }
               is CalenderEvent.ThisYear -> {
                   val thisYear = now.minus(DatePeriod(months = now.month.ordinal,days = now.dayOfMonth-1)).. now
                   _state.update {
                       it.copy(dateRange = thisYear)
                   }
                   _state.value.dateRange?.let {
                       navigateBackWithDateRange(event.navController,it)
                   }
               }
               is CalenderEvent.ToDay -> {
                   val today = now
                   _state.update {
                       it.copy(dateRange = today..today)
                   }
                   _state.value.dateRange?.let {
                       navigateBackWithDateRange(event.navController,it)
                   }
               }
               is CalenderEvent.Yesterday -> {
                   val yesterday = now.minus(DatePeriod(days = 1))
                   _state.update {
                       it.copy(dateRange = yesterday..yesterday)
                   }
                   _state.value.dateRange?.let {
                       navigateBackWithDateRange(event.navController,it)
                   }
               }
               is CalenderEvent.CurrentMonth ->{
                   _state.update {
                       it.copy(currentMonth = now.month.name)
                   }

               }
               is CalenderEvent.CurrentYear ->{
                   _state.update {
                       it.copy(currentYear = "${now.year}")
                   }
               }
               is CalenderEvent.DateSelected ->{
                   _state.value.dateRange?.let {
                       navigateBackWithDateRange(event.navController,it)
                   }

               }
               else -> {}
           }
   }


    private fun selectDate(userSelectedDate: FullDate){
        val selectedDate = userSelectedDate.day ?: now
        val currentDate = _state.value.selectedDate?.day ?: now
        val dateRange =  currentDate .. selectedDate


        if(selectedDate > currentDate){
            dateSelectedCount++
            _state.update {
                it.copy(dateRange = if(dateSelectedCount > 1) {
                    if(selectedDate != currentDate){
                        dateSelectedCount = 0
                    }
                    selectedDate..selectedDate
                } else {
                    dateRange
                })
            }
            _state.update {
                it.copy(selectedDate = userSelectedDate)
            }
        }else{
            _state.update {
                it.copy(selectedDate = userSelectedDate)
            }
            _state.value.selectedDate?.day?.let {date->
                _state.update {
                    it.copy(dateRange = date .. date)
                }
            }

        }
        _state.value.dateRange?.let {dates ->
            for(localDate in dates.iterator() ){
                dateRangeList.add(localDate)
            }
        }
        if(dateRangeList.isNotEmpty()){
            selectedDates()
        }
        dateRangeList.clear()
    }
    private fun selectedDates(){
        _state.update {userDate ->
            userDate.copy(calender = state.value.calender.map {date ->
                date.fullDate.map {
                    //state.value.selectedDate?.day  it.day == selectedDate
                    it.isSelected = it.day in dateRangeList
                }
                date
            })
        }

    }
    private fun navigateBackWithDateRange(navController: NavController,dateRange:ClosedRange<LocalDate>){
       val rangeString = "${dateRange.start},${dateRange.endInclusive}"
        navController
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(key = DATE_RANGE,rangeString)
        navController.popBackStack()
    }
}
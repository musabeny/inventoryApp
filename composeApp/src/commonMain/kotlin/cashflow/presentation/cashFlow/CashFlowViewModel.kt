package cashflow.presentation.cashFlow

import androidx.lifecycle.ViewModel
import cashflow.domain.usecase.CashFlowUseCases
import core.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

class CashFlowViewModel(
    private val useCases: CashFlowUseCases
):ViewModel() {
    private val _state = MutableStateFlow<CashFlowState>(CashFlowState())
    val state = _state.asStateFlow()

    private val now = Clock.System.todayIn(TimeZone.currentSystemDefault())

    init {
       onEvent(CashFlowEvent.ThisWeek)
    }
    fun onEvent(event: CashFlowEvent){
        when(event){
            is CashFlowEvent.GoToDateSelection ->{
                event.navController.navigate(Routes.Calender.route)
            }
            is CashFlowEvent.ThisWeek ->{
                val thisWeek = useCases.dateRange(now)
                _state.update {
                    it.copy(dateRange = thisWeek)
                }
            }

           is CashFlowEvent.Back ->{
               nextOrBack(false)

           }
           is CashFlowEvent.Next -> {
               nextOrBack(true)

           }
           is CashFlowEvent.SelectedDateRange ->{
               _state.update {
                   it.copy(dateRange = event.dateRange)
               }
           }
        }
    }

    private fun nextOrBack(isNext:Boolean){
        println("dateRange ${_state.value.dateRange?.endInclusive}")


        val dateRange = state.value.dateRange
        if(dateRange?.start != dateRange?.endInclusive ){
            _state.update {
                it.copy(dateRange = now..now)
            }
        }else{
           val localDate =   if(isNext){
                _state.value.dateRange?.endInclusive?.plus(DatePeriod(days = 1))
            }else{
                _state.value.dateRange?.endInclusive?.minus(DatePeriod(days = 1))
            }
            localDate?.let {
                _state.update {
                    it.copy(dateRange = localDate .. localDate)
                }
            }

        }
        if((_state.value.dateRange?.endInclusive ?: now) >= now){
            _state.update {
                it.copy(showNextArrow = false)
            }
        }else{
            _state.update {
                it.copy(showNextArrow = true)
            }
        }


    }
}
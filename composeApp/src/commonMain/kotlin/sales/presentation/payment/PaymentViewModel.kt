package sales.presentation.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashflow.data.mapper.formatMonth
import core.navigation.Routes
import core.util.ITEMS
import core.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.json.Json
import sales.domain.useCase.SaleUseCases

class PaymentViewModel(
    private val useCases: SaleUseCases
):ViewModel() {
     private val _state = MutableStateFlow(PaymentState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    init {

        onEvent(PaymentEvent.SelectedDate("${today.dayOfMonth} ${today.month.formatMonth()} ${today.year}"))
    }
    fun onEvent(event: PaymentEvent){
        when(event){
            is PaymentEvent.InitData -> {
               val calculatedItems =  useCases.calculateItemDetail(event.items)
                _state.update {
                    it.copy(items = calculatedItems)
                }
              val total =   _state.value.items.sumOf { it.total ?: 0.0 }
                _state.update {
                    it.copy(total = "$total")
                }
            }
            is PaymentEvent.NavigateBack -> {
               sendEvent(UiEvent.PopBackStack)
            }
            is PaymentEvent.ShowDateDialog -> {
                _state.update {
                    it.copy(showDateDialog = event.show)
                }
            }
            is PaymentEvent.SelectedDate ->{
                _state.update {
                    it.copy(selectedDate = event.date )
                }
                onEvent(PaymentEvent.ShowDateDialog(false))
            }
            is PaymentEvent.NavigateToEditItem -> {
                if(_state.value.items.isEmpty()){
                    return
                }
                event.saveItems(_state.value.items)
                sendEvent(UiEvent.Navigate(Routes.EditItem.route))
            }

        }
    }

    private  fun sendEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}
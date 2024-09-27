package cashflow.presentation.purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.PurchaseItem
import cashflow.domain.repository.PurchaseRepository
import cashflow.domain.usecase.PurchaseUseCase
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
import settings.domain.useCase.ProductUseCase

class PurchaseViewModel(
    private val productUseCase: ProductUseCase,
    private val purchaseUseCase: PurchaseUseCase,
    private val purchaseRepository: PurchaseRepository
):ViewModel() {
    private val _state = MutableStateFlow(PurchaseState())
    val state = _state.asStateFlow()
    private val now = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        for (index in 1..3){
            val item = PurchaseItem()
            _state.value.items.add(item)
        }

        _state.update {
            it.copy(selectedDate = now)
        }
        _state.update {
            it.copy(selectedDateInString = "${now.dayOfMonth}/${now.monthNumber}/${now.year}")
        }

    }

    fun onEvent(event: PurchaseEvent){
         when(event){
            is  PurchaseEvent.GoBack ->{
                sendEvent(UiEvent.PopBackStack)
            }
            is PurchaseEvent.AddPurchaseItem ->{
                val item = PurchaseItem()
                _state.value.items.add(item)
                _state.update {
                    it.copy(rowCount = _state.value.items.size)
                }

            }
            is PurchaseEvent.RemovePurchaseItem ->{
                state.value.items.removeAt(event.index)
                _state.update {
                    it.copy(rowCount = _state.value.items.size)
                }
            }
             is PurchaseEvent.EnterBillTitle ->{
                 _state.update {
                     it.copy(billTitle = event.title)
                 }
             }
             is PurchaseEvent.EnterNote ->{
                 _state.update {
                     it.copy(note = event.note)
                 }
             }
             is PurchaseEvent.ShowDatePicker ->{
                 _state.update { it.copy(showDatePicker = event.show) }
             }
             is PurchaseEvent.SelectedDate ->{
                 event.selectedDate?.let {dateSelected ->
                     val date = productUseCase.dateMillsToDate(dateSelected)
                     val localDate = LocalDate(date.year,date.monthNumber,date.dayOfMonth)
                     _state.update {
                         it.copy(selectedDate = localDate)
                     }
                     state.value.selectedDate?.let {selected ->
                         _state.update {
                             it.copy(selectedDateInString = "${selected.dayOfMonth}/${selected.dayOfMonth}/${selected.year}")
                         }
                     }
                 }



                 onEvent(PurchaseEvent.ShowDatePicker(false))
             }
             is PurchaseEvent.EnterItemName ->{
                 _state.update {
                     it.copy(itemName = event.item)
                 }
                _state.update {
                    it.copy(items = ArrayList(_state.value.items .mapIndexed {index,item -> if(index == event.index) item.copy(name = event.item) else item  }))
                }
             }
             is PurchaseEvent.EnterPrice ->{
                 _state.update {
                     it.copy(items = ArrayList(_state.value.items .mapIndexed {index,item -> if(index == event.index) item.copy(price = event.price) else item  }))
                 }
             }
             is PurchaseEvent.SavePurchase ->savePurchase(event.isDraft)
             else ->{}

         }
    }


    private fun savePurchase(isDraft:Boolean){
        val billTitle = _state.value.billTitle
        _state.update {
            it.copy(billError = purchaseUseCase.validateBill(billTitle))
        }
       val validateAddItem =  _state.value.items.any { it.name.isNotBlank() && it.price.isNotBlank() }
        _state.update {
            it.copy(itemError = purchaseUseCase.validateItemAndPrice(validateAddItem))
        }

        val hasError = listOf(
            _state.value.billError,
            _state.value.itemError
        ).any { it != null }

        if(hasError){
            _state.value.itemError?.let {
                sendEvent(UiEvent.ShowSnackBar(it))
            }
            return
        }
        val res = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val bill = Bill(
            id = null,
            billTitle = billTitle,
            note = _state.value.note,
            isDraft = isDraft,
            dateCreated = _state.value.selectedDate ?: Clock.System.todayIn(TimeZone.currentSystemDefault())
        )
//        purchaseRepository.savePurchase(bill,_state.value.items)

    }

    private  fun sendEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }

}
package cashflow.presentation.purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashflow.data.mapper.toBillItem
import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.BillItem
import cashflow.domain.model.purchase.PurchaseItem
import cashflow.domain.repository.PurchaseRepository
import cashflow.domain.usecase.PurchaseUseCase
import core.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
                onEvent(PurchaseEvent.ShowDialog(false))
                if(event.purchaseItem?.id == null){
                    return
                }
                onEvent(PurchaseEvent.RemoveItem(event.purchaseItem.toBillItem()))
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
             is PurchaseEvent.GetPurchaseById ->{
                 event.billId?.let {
                     purchaseRepository.findPurchases(event.billId).onEach { billAndItems ->
                      _state.update {
                          it.copy(billItems = billAndItems)
                      }

                         _state.value.billItems?.let { billItems ->
                             _state.update {
                                 it.copy(rowCount = billItems.items.size )
                             }

                             _state.update {
                                 it.copy(billTitle = billItems.bill.billTitle )
                             }
                             _state.update {
                                 it.copy(note = billItems.bill.note ?: "" )
                             }
                             _state.update {
                                 it.copy(selectedDate = billItems.bill.dateCreated)
                             }
                             _state.value.items.clear()
                             billItems.items.forEach { item ->
                                val billItem =  PurchaseItem(
                                    id = item.id,
                                    name = item.itemName,
                                    price = item.price.toString(),
                                    billId = item.billId
                                )
                                _state.value.items.add(billItem)
                             }
                         }


                     }.launchIn(viewModelScope)
                 }

             }
             is PurchaseEvent.ShowDialog ->{
                 _state.update {
                     it.copy(showDialog = event.show)
                 }
                 if(_state.value.showDialog){
                     _state.update {
                         it.copy(selectedItem = event.purchaseItem?.toBillItem())
                     }
                     _state.update {
                         it.copy(selectedIndex = event.index)
                     }
                 }else{
                     _state.update {
                         it.copy(selectedItem = null)
                     }
                     _state.update {
                         it.copy(selectedIndex = null)
                     }
                 }
             }
             is PurchaseEvent.RemoveItem ->{
                 viewModelScope.launch {
                     purchaseRepository.deleteItem(event.item)
                     onEvent(PurchaseEvent.ShowDialog(false))
                     _state.update {
                         it.copy(selectedItem = null)
                     }
                 }
             }
             is PurchaseEvent.SelectedItemToRemove ->{
                 _state.update {
                     it.copy(selectedItem = event.item)
                 }
                 onEvent(PurchaseEvent.ShowDialog(true))
             }
             else ->{}

         }
    }


    private fun savePurchase(isDraft:Boolean){
        viewModelScope.launch {
            val billTitle = _state.value.billTitle
            val items = ArrayList<BillItem>()
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
                return@launch
            }
            val bill = Bill(
                id = _state.value.billItems?.bill?.id,
                billTitle = billTitle,
                note = _state.value.note,
                isDraft = isDraft,
                dateCreated = _state.value.selectedDate ?: Clock.System.todayIn(TimeZone.currentSystemDefault())
            )
            _state.value.items.filter { it.name.isNotBlank() || it.price.isNotBlank() }.forEach { item ->
                val billItem = BillItem(
                    id=item.id,
                    itemName = item.name,
                    price = item.price.toDoubleOrNull() ?: 0.0,
                    billId = _state.value.billItems?.bill?.id
                )
                items.add(billItem)
            }

            if(_state.value.billItems?.bill?.id == null){
                purchaseRepository.savePurchase(bill,items)
            }else{
                purchaseRepository.updateBillWithItems(bill,items)
            }
            sendEvent(UiEvent.PopBackStack)

        }

    }

    private  fun sendEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }

}
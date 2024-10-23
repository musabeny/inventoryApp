package cashflow.presentation.purchaseReceipt

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashflow.domain.repository.PurchaseRepository
import core.navigation.Routes
import core.util.BILL_ID
import core.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PurchaseReceiptViewModel(
    private val purchaseRepository: PurchaseRepository
):ViewModel() {
    private val _state = MutableStateFlow(PurchaseReceiptState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: PurchaseReceiptEvent){
        when(event){
            is PurchaseReceiptEvent.BillWithItems ->{
                event.billId?.let {
                    purchaseRepository.findPurchases(event.billId).onEach { bill ->
                        _state.update {
                            it.copy(billItems = bill)
                        }
                    }.launchIn(viewModelScope)
                }

            }
            is PurchaseReceiptEvent.NavigateBack ->{
                sendEvent(UiEvent.PopBackStack)
            }
            is PurchaseReceiptEvent.NavigateToAddPurchase ->{
                sendEvent(UiEvent.PopBackStack)
                sendEvent(UiEvent.Navigate(Routes.Purchase.route.replace("{$BILL_ID}","${event.billId}")))
            }
        }
    }

    private  fun sendEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}
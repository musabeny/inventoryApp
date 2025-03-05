package sales.presentation.editItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sales.presentation.payment.PaymentEvent

class EditItemViewModel():ViewModel() {
    private val _state = MutableStateFlow(EditItemState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: EditItemEvent){
        when(event){
            is EditItemEvent.GetItems -> {
                _state.update {
                    it.copy(items = event.items)
                }
            }
            is EditItemEvent.NavigateBack ->{
                sendEvent(UiEvent.PopBackStack)
            }
            is EditItemEvent.UpdateQuantity ->{
                if(event.isEditText){
                    _state.update { items ->
                        items.copy(items = _state.value.items.mapIndexed { index, item ->
                            if(event.index == index)
                            item.copy(quantity = if(event.quantity>=0)event.quantity else 0)
                            else item
                        })
                    }
                }else{
                    _state.update { items ->
                        items.copy(items = _state.value.items.mapIndexed { index, item ->
                            if(index == event.index)
                                item.copy(
                                    quantity = if(item.quantity != null && item.quantity > 0) item.quantity  + event.quantity
                                    else if(item.quantity != null && event.quantity > 0) item.quantity+event.quantity
                                    else item.quantity
                                )
                            else item

                        })
                    }
                }

            }
            is EditItemEvent.Save->{
                event.saveItems(_state.value.items)
                onEvent(EditItemEvent.NavigateBack)
            }
        }
    }

    private  fun sendEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}
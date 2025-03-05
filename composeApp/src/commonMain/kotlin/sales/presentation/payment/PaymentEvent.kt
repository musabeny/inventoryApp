package sales.presentation.payment

import sales.domain.model.ItemDetail

sealed interface PaymentEvent {
    data class InitData(val items:List<ItemDetail>):PaymentEvent
    data object NavigateBack:PaymentEvent
    data class ShowDateDialog(val show:Boolean):PaymentEvent
    data class SelectedDate(val date:String):PaymentEvent
    data class NavigateToEditItem(val saveItems:(List<ItemDetail>) -> Unit):PaymentEvent
}
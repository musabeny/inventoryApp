package cashflow.presentation.purchase

import androidx.navigation.NavController
import cashflow.domain.model.purchase.PurchaseItem

sealed interface PurchaseEvent {

    data object GoBack:PurchaseEvent
    data object AddPurchaseItem:PurchaseEvent
    data class RemovePurchaseItem(val index:Int):PurchaseEvent
    data class EnterBillTitle(val title:String):PurchaseEvent
    data class EnterNote(val note:String):PurchaseEvent
    data class ShowDatePicker(val show:Boolean):PurchaseEvent
    data class SelectedDate(val selectedDate:Long?):PurchaseEvent
    data class EnterItemName(val item:String,val index: Int):PurchaseEvent
    data class EnterPrice(val price:String,val index: Int):PurchaseEvent
    data class SavePurchase(val isDraft:Boolean):PurchaseEvent
}
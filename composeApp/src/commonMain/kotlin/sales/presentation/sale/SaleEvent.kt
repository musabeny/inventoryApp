package sales.presentation.sale

import sales.domain.enums.ClearAction
import sales.domain.model.ItemDetail

sealed interface SaleEvent {
    data class Operator(val operator:String): SaleEvent
    data class Clear(val clear: ClearAction,val isChangePrice:Boolean = false): SaleEvent
    data class Operand(val operand:String,val isChangePrice:Boolean = false): SaleEvent
    data class SpecialCharacter(val value:String,val isChangePrice:Boolean = false): SaleEvent
    data object AddItem: SaleEvent
    data class ShowEnterPriceDialog(val show:Boolean): SaleEvent
    data class ShowSheet(val show:Boolean): SaleEvent
    data class Navigate(val saveItems:(List<ItemDetail>)->Unit): SaleEvent
    data class UpdatedItems(val items:List<ItemDetail>):SaleEvent
    data object SaveTemporaryPrice:SaleEvent
}
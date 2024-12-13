package sales.presentation

import sales.domain.enums.ClearAction

sealed interface SaleEvent {
    data class Operator(val operator:String):SaleEvent
    data class Clear(val clear: ClearAction,val isChangePrice:Boolean = false):SaleEvent
    data class Operand(val operand:String,val isChangePrice:Boolean = false):SaleEvent
    data class SpecialCharacter(val value:String,val isChangePrice:Boolean = false):SaleEvent
    data object AddItem:SaleEvent
    data class ShowEnterPriceDialog(val show:Boolean):SaleEvent
}
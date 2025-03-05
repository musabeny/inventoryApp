package sales.presentation.editItem

import sales.domain.model.ItemDetail

sealed interface EditItemEvent {
    data class GetItems(val items:List<ItemDetail>):EditItemEvent
    data object NavigateBack:EditItemEvent
    data class UpdateQuantity(val quantity:Int,val index:Int,val isEditText:Boolean = false):EditItemEvent
    data class Save(val saveItems:(List<ItemDetail>) -> Unit):EditItemEvent
}
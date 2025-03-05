package sales.presentation.editItem

import sales.domain.model.ItemDetail

data class EditItemState(
    val items:List<ItemDetail> = emptyList()
)

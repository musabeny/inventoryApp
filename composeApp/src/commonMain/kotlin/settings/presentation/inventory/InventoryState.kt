package settings.presentation.inventory

import settings.domain.model.product.Product

data class InventoryState(
    val data:String = "",
    val products:List<Product> = emptyList(),
    val showSearchIcon:Boolean = true,
    val searchText:String = "",
    val showOnlyExpired:Boolean = false
)

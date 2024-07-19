package settings.presentation.inventory

import androidx.navigation.NavHostController

sealed interface InventoryEvent {
    data class GoToAddItem(val navController: NavHostController):InventoryEvent
    data object GetProduct:InventoryEvent
    data class SelectProduct(val navController: NavHostController,val productId:Long):InventoryEvent
    data object ShowSearchIcon:InventoryEvent
    data object ClearSearchText:InventoryEvent
    data class EnterSearchText(val searchText:String):InventoryEvent
    data object ShowOnlyExpired:InventoryEvent
}
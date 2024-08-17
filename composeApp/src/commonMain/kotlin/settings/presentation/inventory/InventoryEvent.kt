package settings.presentation.inventory

import androidx.navigation.NavHostController
import settings.domain.model.category.CategoryWithColor
import settings.domain.model.product.ProductColor

sealed interface InventoryEvent {
    data class GoToAddItem(val navController: NavHostController):InventoryEvent
    data object GetProduct:InventoryEvent
    data class SelectProduct(val navController: NavHostController,val productId:Long):InventoryEvent
    data object ShowSearchIcon:InventoryEvent
    data object ClearSearchText:InventoryEvent
    data class EnterSearchText(val searchText:String):InventoryEvent
    data object ShowOnlyExpired:InventoryEvent
    data class ShowBottomSheet(val bottomSheet:Boolean):InventoryEvent
    data object SaveCategories:InventoryEvent
    data object GetProductColor:InventoryEvent
    data class SelectColor(val productColor: ProductColor):InventoryEvent
    data class EnterCategoryName(val categoryName:String):InventoryEvent
    data object GetAllCategories:InventoryEvent
    data class SelectCategory(val category: CategoryWithColor):InventoryEvent
    data class DeleteCategory(val category: CategoryWithColor):InventoryEvent
    data class CurrentPage(val index:Int):InventoryEvent
}
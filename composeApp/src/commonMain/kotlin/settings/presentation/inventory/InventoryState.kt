package settings.presentation.inventory

import org.jetbrains.compose.resources.StringResource
import settings.domain.enums.InventorySearchType
import settings.domain.model.category.CategoryWithColor
import settings.domain.model.product.Product
import settings.domain.model.product.ProductColor

data class InventoryState(
    val data:String = "",
    val products:List<Product> = emptyList(),
    val showSearchIcon:Boolean = true,
    val searchText:String = "",
    val showOnlyExpired:Boolean = false,
    val showBottomSheet:Boolean = false,
    val productColors:List<ProductColor> = emptyList(),
    val selectedProductColor:ProductColor? = null,
    val categoryName:String = "",
    val categoryError:StringResource? = null,
    val categories:List<CategoryWithColor> = emptyList(),
    val selectedCategory: CategoryWithColor? = null,
    val searchType:InventorySearchType = InventorySearchType.INVENTORY
)

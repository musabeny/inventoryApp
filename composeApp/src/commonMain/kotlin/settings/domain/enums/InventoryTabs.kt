package settings.domain.enums

import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.categories
import inventoryapp.composeapp.generated.resources.inventory
import inventoryapp.composeapp.generated.resources.purchase_item
import org.jetbrains.compose.resources.StringResource

enum class InventoryTabs(
    val label:StringResource
) {
    INVENTORY(
        label = Res.string.inventory
    ),
    CATEGORIES(
    label = Res.string.categories
    ),
    PURCHASE_ITEMS(
        label = Res.string.purchase_item
    )
}
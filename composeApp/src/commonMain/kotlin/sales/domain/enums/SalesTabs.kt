package sales.domain.enums

import core.util.UiText
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.calculator
import inventoryapp.composeapp.generated.resources.sales
import inventoryapp.composeapp.generated.resources.saved
import org.jetbrains.compose.resources.StringResource

enum class SalesTabs(val label:StringResource) {
    CALCULATOR(
        label = Res.string.calculator
    ),
    SALES(
        label = Res.string.sales
    ),
    SAVED(
        label = Res.string.saved
    )
}
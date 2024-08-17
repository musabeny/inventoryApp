package cashflow.domain.enums

import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.categories
import inventoryapp.composeapp.generated.resources.income
import inventoryapp.composeapp.generated.resources.income_expense
import inventoryapp.composeapp.generated.resources.inventory
import inventoryapp.composeapp.generated.resources.purchase
import inventoryapp.composeapp.generated.resources.purchase_item
import org.jetbrains.compose.resources.StringResource

enum class CashFlowTabs(
    val label:StringResource
) {
    INCOME(
        label = Res.string.income_expense
    ),
    PURCHASE(
    label = Res.string.purchase
    ),

}
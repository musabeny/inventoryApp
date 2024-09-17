package core.navigation

import core.util.CATEGORY_ID
import core.util.IS_INCOME_OR_EXPENSE
import core.util.PRODUCT_ID
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.calculate
import inventoryapp.composeapp.generated.resources.cash_flow
import inventoryapp.composeapp.generated.resources.more
import inventoryapp.composeapp.generated.resources.report
import inventoryapp.composeapp.generated.resources.report_icon
import inventoryapp.composeapp.generated.resources.sales
import inventoryapp.composeapp.generated.resources.inventory
import inventoryapp.composeapp.generated.resources.product
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class Routes(
    val route: String,
    var title: StringResource,
    val defaultIcon: DrawableResource?
) {
    data object Report:Routes(route = "report",
        title = Res.string.report,
        defaultIcon = Res.drawable.report_icon
    )

    data object Sales:Routes(
        route = "sales",
        title = Res.string.sales,
        defaultIcon = Res.drawable.calculate
    )

    data object CashFlow:Routes(route = "cash-flow",
        title = Res.string.cash_flow,
        defaultIcon = Res.drawable.cash_flow
    )
    data object Settings:Routes(route = "settings",
        title = Res.string.more,
        defaultIcon = Res.drawable.more
    )

    data object Inventory:Routes(route = "inventory",
        title = Res.string.inventory,
        defaultIcon = null
    )
    data object Product:Routes(route = "product/{$PRODUCT_ID}",
        title = Res.string.product,
        defaultIcon = null
    )

    data object Calender:Routes(route = "calender",
        title = Res.string.inventory,
        defaultIcon = null
    )

    data object BreakDown:Routes(
        route = "breakDown/{$CATEGORY_ID}/{$IS_INCOME_OR_EXPENSE}",
        title = Res.string.inventory,
        defaultIcon = null
    )
}
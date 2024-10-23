package cashflow.presentation.purchase

import cashflow.domain.model.purchase.BillAndItems
import cashflow.domain.model.purchase.BillItem
import cashflow.domain.model.purchase.PurchaseItem
import core.util.UiText
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.StringResource

data class PurchaseState(
    val rowCount:Int = 3,
    val items:ArrayList<PurchaseItem> = ArrayList(),
    val billTitle:String = "",
    val note:String = "",
    val showDatePicker:Boolean = false,
    val selectedDate:LocalDate? = null,
    val selectedDateInString:String = "",
    val itemName:String = "",
    val price:String = "",
    val billError:UiText? = null,
    val itemError:UiText? = null,
    val billItems: BillAndItems? = null,
    val showDialog:Boolean = false,
    val selectedItem: BillItem? = null,
    val selectedIndex:Int? = null
)

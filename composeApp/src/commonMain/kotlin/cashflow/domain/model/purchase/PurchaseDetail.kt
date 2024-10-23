package cashflow.domain.model.purchase

import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.StringResource

data class PurchaseDetail(
    val day:String,
    val dayName:StringResource?,
    val monthYear:String,
    val total:Double,
    val itemCount:String,
    val note:String?,
    val isDaft:Boolean,
    val billName:String,
    val billId:String?,
    val dateCreated:LocalDate
)



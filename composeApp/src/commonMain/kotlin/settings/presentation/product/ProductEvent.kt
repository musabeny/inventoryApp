package settings.presentation.product

import androidx.compose.ui.graphics.Color
import settings.domain.model.product.ProductColor

sealed interface ProductEvent {
    data object AddProduct:ProductEvent
    data class  EnterItemCode(val code:String):ProductEvent
    data class EnterItemName(val name:String):ProductEvent
    data class EnterItemPrice(val price:String):ProductEvent
    data object PriceCheckOption:ProductEvent
    data class SelectColor(val color: ProductColor):ProductEvent
    data object GetProductColors:ProductEvent
    data class EnterBarcode(val barcode:String):ProductEvent
    data class ScanBarcode(val barcode: String):ProductEvent
    data class SelectExpireDate(val date:String):ProductEvent
    data class EnterExpireDateAlert(val days:String):ProductEvent
    data class DatePickerDialog(val show:Boolean):ProductEvent
    data class SelectedDate(val dateSelected:Long?):ProductEvent
    data object IsPriceRequired:ProductEvent
    data object LastItemCode:ProductEvent
    data class  FindItemCode(val itemCode:String,val userSearch:Boolean):ProductEvent
    data class GetProductById(val productId:Long):ProductEvent
}
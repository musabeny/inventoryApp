package settings.presentation.product

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.StringResource
import settings.domain.model.product.ProductColor

data class ProductState(
    val productId:Long? = null,
    val code:String = "",
    val codeError:StringResource? = null,
    val name:String="",
    val nameError:StringResource? = null,
    val price:String?=null,
    val priceError:StringResource? = null,
    val priceCheck:Boolean = false,
    val color:ProductColor = ProductColor(1, Color.Red),
    val barcode:String? = null,
    val expireDate:Long? = null,
    val expireDateFormatted:String? = null,
    val expireDateAlert:String? = null,
    val dateCreated:Long = 0L,
    val validationSuccess:Boolean = false,
    val productColors:List<ProductColor> = emptyList(),
    val showDatePickerDialog:Boolean = false,
    val priceNotRequired:Boolean = false,
    val itemCodeFoundError:StringResource? = null,


    )

package sales.presentation

import sales.domain.model.ProductCount
import settings.domain.model.product.Product

data class SaleState(
    val tapValue:StringBuilder = StringBuilder(),
    val item:String = "",
    val enableOperator:Boolean = false,
    val items:List<String> = emptyList(),
    val totalCash:String = "",
    val  enableZBtn:Boolean = true,
    val enableAtBtn:Boolean = false,
    val enableDotBtn:Boolean = true,
    val enableDotBtnEnterPrice:Boolean = false,
    val enableEnterBtnEnterPrice:Boolean = false,
    val enableEnterBtn:Boolean = false,
    val showEnterPriceDialog:Boolean = false,
    val zType:String? = null,
    val changePrice:String = "",
    val product: Product? = null,
    val products:List<ProductCount> = emptyList()
)

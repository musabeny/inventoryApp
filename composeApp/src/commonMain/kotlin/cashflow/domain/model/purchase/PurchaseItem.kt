package cashflow.domain.model.purchase

data class PurchaseItem(
    val id:Long? = null,
    val name:String = "",
    val price:String = "",
    val billId:Long? = null
)

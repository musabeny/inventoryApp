package sales.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class ItemDetail(
    val productId:Long?,
    val product: String?,
    val total : Double?,
    val expression:String?,
    val itemPrice:Double?,
    val quantity:Int?
)

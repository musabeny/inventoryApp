package settings.domain.model.product

data class Product(
   val id:Long?,
    val code:String,
    val name:String,
    val price:Double?,
    val color:ProductColor,
    val barcode:String?,
    val expireDate:Long?,
    val expireDateAlert:Int?,
  val  dateCreated:Long
)

package cashflow.domain.model.purchase

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

data class BillItem(
   val id:Long? ,
    val itemName:String,
    val price:Double,
    val billId:Long?
)

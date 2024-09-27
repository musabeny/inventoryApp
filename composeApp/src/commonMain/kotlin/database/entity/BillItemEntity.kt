package database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity
data class BillItemEntity(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    val itemName:String,
    val price:Double,
    val billId:Long
)

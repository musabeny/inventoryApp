package cashflow.domain.model.purchase

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

data class Bill(
     val id:Long?,
    val billTitle:String,
    val note:String,
    val isDraft:Boolean,
    val dateCreated: LocalDate,
)

package sales.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

data class TemporaryPrice(
    val id:Long?  = null,
    val code:String,
    val price:Double
)
package database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["code" ], unique = true)]
)
data class TemporaryPriceEntity(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    val code:String,
    val price:Double
)
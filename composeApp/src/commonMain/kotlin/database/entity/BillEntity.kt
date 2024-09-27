package database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity
data class BillEntity(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    val billTitle:String,
    val note:String,
    val isDraft:Boolean,
    val dateCreated: LocalDate,
)

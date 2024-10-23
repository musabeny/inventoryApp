package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = BillEntity::class,
            parentColumns = ["id"],
            childColumns = ["bill_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bill_id"])]
)
data class BillItemEntity(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    val itemName:String,
    val price:Double,
    @ColumnInfo(name = "bill_id") val billId:Long?
)

@Entity
@Fts4(contentEntity = BillItemEntity::class)
data class BillItemFts(
    @ColumnInfo(name = "rowid")
    @PrimaryKey val id:Long,
    val itemName:String
)

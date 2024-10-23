package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity
data class BillEntity(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    val billTitle:String,
    val note:String?,
    val isDraft:Boolean,
    val dateCreated: LocalDate,
)

@Entity
@Fts4(contentEntity = BillEntity::class)
data class BillFts(
    @ColumnInfo(name = "rowid")
    @PrimaryKey val id:Long,
    val billTitle:String,
    val note:String?,
)

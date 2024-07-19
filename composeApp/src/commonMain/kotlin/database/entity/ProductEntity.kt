package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock

@Entity(
  indices = [Index(value = ["code" ], unique = true)]
)
data class ProductEntity(
  @PrimaryKey(autoGenerate = true)  val id:Long = 0L,
    val code:String,
    val name:String,
    val price:Double?,
    val color:Int,
    val barcode:String?,
    val expireDate:Long?,
    val expireDateAlert:Int?,
  val   dateCreated:Long = Clock.System.now().toEpochMilliseconds()
)


@Entity
@Fts4(contentEntity = ProductEntity::class)
data class ProductFts(
  @ColumnInfo(name = "rowid")
  @PrimaryKey val id:Long,
  val name:String
)

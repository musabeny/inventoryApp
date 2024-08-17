package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    indices = [Index(value = ["name" ], unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    val name:String,
    val colorId:Int
)

@Entity
@Fts4(contentEntity = CategoryEntity::class)
data class CategoryFts(
    @ColumnInfo(name = "rowid")
    @PrimaryKey val id:Long,
    val name:String
)
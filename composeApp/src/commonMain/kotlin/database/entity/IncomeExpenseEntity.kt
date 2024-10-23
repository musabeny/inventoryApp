package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity
data class IncomeExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    val categoryId:Long,
    val amount:Double,
    val note:String?,
    val isIncomeOrExpense:Int,
    val dateCreated:LocalDate

)


@Entity
@Fts4(contentEntity = IncomeExpenseEntity::class)
data class IncomeExpenseFts(
    @ColumnInfo(name = "rowid")
    @PrimaryKey val id:Long,
    val note:String?,
)
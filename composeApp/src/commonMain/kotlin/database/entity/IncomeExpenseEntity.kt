package database.entity

import androidx.room.Entity
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
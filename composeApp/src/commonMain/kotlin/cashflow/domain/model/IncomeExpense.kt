package cashflow.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import settings.domain.model.category.Category
import settings.domain.model.category.CategoryWithColor

data class IncomeExpense(
     val id:Long? ,
    val category:CategoryWithColor,
    val amount:Double,
    val note:String?,
    val isIncomeOrExpense:Int,
    val dateCreated:LocalDate,


)
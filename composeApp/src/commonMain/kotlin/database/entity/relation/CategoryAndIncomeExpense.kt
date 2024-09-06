package database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import database.entity.CategoryEntity
import database.entity.IncomeExpenseEntity

data class CategoryAndIncomeExpense(
    @Embedded val incomeExpense:IncomeExpenseEntity,
    @Relation(
      parentColumn = "categoryId",
      entityColumn = "id"
    )
    val category: CategoryEntity
)


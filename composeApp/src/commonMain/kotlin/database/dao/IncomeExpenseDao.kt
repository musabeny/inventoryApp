package database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import database.entity.CategoryEntity
import database.entity.IncomeExpenseEntity
import database.entity.relation.CategoryAndIncomeExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface IncomeExpenseDao {

    @Insert
    suspend fun insertIncomeExpense(incomeExpenseEntity: IncomeExpenseEntity):Long

    @Delete
    suspend fun deleteIncomeExpense(incomeExpenseEntity: IncomeExpenseEntity):Int

    @Transaction
    @Query("SELECT * FROM IncomeExpenseEntity WHERE dateCreated BETWEEN :startDate AND :endDate ORDER BY dateCreated DESC  ")
    fun getIncomeExpense(startDate:LocalDate,endDate:LocalDate):Flow<List<CategoryAndIncomeExpense>>


    @Transaction
    @Query("SELECT * FROM IncomeExpenseEntity WHERE isIncomeOrExpense =:expenseOrIncomeId ")
    fun getCategoryByExpenseOrIncome(expenseOrIncomeId:Int):Flow<List<CategoryAndIncomeExpense>>

}

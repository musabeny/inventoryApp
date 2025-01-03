package database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import database.entity.IncomeExpenseEntity
import database.entity.relation.IncomeExpenseAndCategory
import database.model.CategoryIdAndIsIncomeOrExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface IncomeExpenseDao {

    @Insert
    suspend fun insertIncomeExpense(incomeExpenseEntity: IncomeExpenseEntity):Long

    @Delete
    suspend fun deleteIncomeExpense(vararg incomeExpenseEntity: IncomeExpenseEntity):Int

    @Transaction
    @Query("SELECT * FROM IncomeExpenseEntity WHERE dateCreated BETWEEN :startDate AND :endDate ORDER BY dateCreated DESC  ")
    fun getIncomeExpense(startDate:LocalDate,endDate:LocalDate):Flow<List<IncomeExpenseAndCategory>>


    @Transaction
    @Query("SELECT * FROM IncomeExpenseEntity WHERE isIncomeOrExpense =:expenseOrIncomeId ")
    fun getCategoryByExpenseOrIncome(expenseOrIncomeId:Int):Flow<List<IncomeExpenseAndCategory>>

    @Transaction
    @Query("SELECT * FROM IncomeExpenseEntity WHERE  dateCreated BETWEEN :startDate AND :endDate  AND categoryId IN (:category) AND isIncomeOrExpense IN (:incomeOrExpense) ORDER BY dateCreated DESC  ")
    fun getIncomeExpenseByCategory(startDate:LocalDate,endDate:LocalDate,category:List<Long>,incomeOrExpense:List<Int>):Flow<List<IncomeExpenseAndCategory>>

    @Transaction
    @Query("SELECT * FROM IncomeExpenseEntity WHERE dateCreated BETWEEN :startDate AND :endDate AND isIncomeOrExpense IN (:incomeOrExpense)  ORDER BY dateCreated DESC  ")
    fun getIncomeExpenseByEntryType(startDate:LocalDate,endDate:LocalDate,incomeOrExpense:List<Int>):Flow<List<IncomeExpenseAndCategory>>

    @Transaction
    @Query("SELECT * FROM IncomeExpenseEntity WHERE dateCreated BETWEEN :startDate AND :endDate AND isIncomeOrExpense IN (:incomeOrExpense) AND categoryId IN (:categories)  ORDER BY dateCreated DESC  ")
    fun getIncomeExpenseFiltered(startDate:LocalDate,endDate:LocalDate,incomeOrExpense:List<Int>, categories:List<Long>):Flow<List<IncomeExpenseAndCategory>>

    @Transaction
    @Query("SELECT * FROM IncomeExpenseEntity WHERE categoryId =:categoryId AND isIncomeOrExpense =:isIncomeOrExpense")
    fun getIncomeExpenseByCategoryId(categoryId:Long,isIncomeOrExpense:Int):Flow<List<IncomeExpenseAndCategory>>

    @Delete(
        entity = IncomeExpenseEntity::class
    )
    suspend fun deleteByCategoryId(vararg  categoryIdAndIsIncomeOrExpense: CategoryIdAndIsIncomeOrExpense):Int

    @Query("""
        SELECT * FROM IncomeExpenseEntity
        JOIN IncomeExpenseFts ON IncomeExpenseFts.note == IncomeExpenseEntity.note
        WHERE dateCreated BETWEEN :startDate AND :endDate AND IncomeExpenseFts.note MATCH :searchText
    """)
    fun searchOnIncomeExpense(
        startDate: LocalDate,
        endDate: LocalDate,
        searchText:String
    ):Flow<List<IncomeExpenseAndCategory>>




}

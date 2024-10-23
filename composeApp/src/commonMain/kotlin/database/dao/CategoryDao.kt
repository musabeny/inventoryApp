package database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import database.entity.CategoryEntity
import database.entity.ProductEntity
import database.entity.relation.BillWithItems
import database.entity.relation.IncomeExpenseAndCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: CategoryEntity):Long

    @Update
    suspend fun updateCategory(category: CategoryEntity):Int

    @Query("SELECT * FROM CategoryEntity ")
    fun getAllCategory():Flow<List<CategoryEntity>>

    @Delete
    suspend fun deleteCategory(category: CategoryEntity):Int

    @Query("""
        SELECT * FROM CategoryEntity
        JOIN CategoryFts ON CategoryFts.name == CategoryEntity.name
        WHERE CategoryFts.name MATCH :name
    """)
    fun searchCategoryByName(name:String):Flow<List<CategoryEntity>>


    @Transaction
    @Query("SELECT * FROM IncomeExpenseEntity WHERE dateCreated BETWEEN :startDate AND :endDate AND categoryId IN (:categoryIds) ORDER BY dateCreated DESC ")
    fun searchOnIncomeExpenseByCategory(startDate: LocalDate, endDate: LocalDate, categoryIds:List<Long>):Flow<List<IncomeExpenseAndCategory>>

    @OptIn(ExperimentalCoroutinesApi::class)
    fun searchByCategoryName(startDate: LocalDate, endDate: LocalDate, queryText:String):Flow<List<IncomeExpenseAndCategory>>{
        val categories = searchCategoryByName(queryText)
       val incomeExpense = categories.map { category ->
            val categoryIds = category.map { it.id }
            searchOnIncomeExpenseByCategory(startDate,endDate,categoryIds.map { it ?: 0L })
        }
        return incomeExpense.flattenConcat()
    }


}
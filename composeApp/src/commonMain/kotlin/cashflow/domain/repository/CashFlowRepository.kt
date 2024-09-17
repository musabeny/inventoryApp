package cashflow.domain.repository

import cashflow.domain.model.FilterType
import cashflow.domain.model.IncomeExpense
import database.model.CategoryIdAndIsIncomeOrExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import settings.domain.model.category.Category

interface CashFlowRepository {
    suspend fun insertIncomeExpense(incomeExpense: IncomeExpense):Long
    suspend fun deleteIncomeExpense(incomeExpense: IncomeExpense):Int
    fun getIncomeExpense(startDate:LocalDate,endDate:LocalDate):Flow<List<IncomeExpense>>
    fun getCategoryByIncomeOrExpense(expenseOrIncome:Int):Flow<List<FilterType>>
    fun filterIncomeExpense(startDate:LocalDate,endDate:LocalDate,entryTypes:List<Int>,category:List<Long>,categoryIncomeExpenseType:List<Int>):Flow<List<IncomeExpense>>
   suspend fun deleteIncomeCategoryById(incomeExpense: IncomeExpense):Int
    fun getIncomeExpenseByCategoryId(categoryId:Long,isIncomeOrExpense:Int):Flow<List<IncomeExpense>>
    suspend fun deleteByCategoryIdAndIsIncomeOrExpense(categoryIdAndIsIncomeOrExpense: CategoryIdAndIsIncomeOrExpense):Int
}
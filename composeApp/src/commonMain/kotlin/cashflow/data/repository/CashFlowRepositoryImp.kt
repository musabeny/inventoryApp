package cashflow.data.repository

import cashflow.data.mapper.toBillEntity
import cashflow.data.mapper.toBillItemEntity
import cashflow.data.mapper.toFilterType
import cashflow.data.mapper.toIncomeExpense
import cashflow.data.mapper.toIncomeExpenseEntity
import cashflow.domain.enums.IncomeExpenseType
import cashflow.domain.model.FilterType
import cashflow.domain.model.IncomeExpense
import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.BillItem
import cashflow.domain.repository.CashFlowRepository
import database.InventoryDatabase
import database.model.CategoryIdAndIsIncomeOrExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.datetime.LocalDate
import settings.domain.useCase.ProductUseCase

 class CashFlowRepositoryImp(
    private val db:InventoryDatabase,
    private val productUseCase: ProductUseCase
): CashFlowRepository {
    override suspend fun insertIncomeExpense(incomeExpense: IncomeExpense): Long {
      return  db.incomeExpenseDao().insertIncomeExpense(incomeExpense.toIncomeExpenseEntity())
    }

    override suspend fun deleteIncomeExpense(incomeExpense: IncomeExpense): Int {
        return db.incomeExpenseDao().deleteIncomeExpense(incomeExpense.toIncomeExpenseEntity())
    }

    override fun getIncomeExpense(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<IncomeExpense>> {
        //db.incomeExpenseDao().incomeExpense()
        return  db.incomeExpenseDao().getIncomeExpense(startDate,endDate).map {
            it.map { incomeExpense ->
                incomeExpense.toIncomeExpense(productUseCase.color())
            }
        }
    }

    override fun getCategoryByIncomeOrExpense(expenseOrIncome: Int): Flow<List<FilterType>> {
       return db.incomeExpenseDao().getCategoryByExpenseOrIncome(expenseOrIncome).map { incomeExpense ->
           incomeExpense.map {it.toFilterType()  }
       }
    }

     override fun filterIncomeExpense(
         startDate:LocalDate,
         endDate:LocalDate,
         entryTypes: List<Int>,
         category: List<Long>,
         categoryIncomeExpenseType:List<Int>
     ): Flow<List<IncomeExpense>> {
         return if(entryTypes.isNotEmpty()  && category.isEmpty()){
             db.incomeExpenseDao().getIncomeExpenseByEntryType(startDate, endDate,entryTypes).map { byEntry -> byEntry.map { it.toIncomeExpense(productUseCase.color()) } }
         }else if(category.isNotEmpty() && entryTypes.isEmpty()){
             db.incomeExpenseDao().getIncomeExpenseByCategory(startDate, endDate,category,categoryIncomeExpenseType).map { byCategory -> byCategory.map { it.toIncomeExpense(productUseCase.color()) } }
         }else if(entryTypes.isNotEmpty() && category.isNotEmpty()){
             db.incomeExpenseDao().getIncomeExpenseFiltered(startDate, endDate,entryTypes,category).map { expenseIncome -> expenseIncome.map { it.toIncomeExpense(productUseCase.color()) } }
         }else{
             db.incomeExpenseDao().getIncomeExpense(startDate, endDate).map { expenseIncome -> expenseIncome.map { it.toIncomeExpense(productUseCase.color()) } }
         }


     }

     override suspend fun deleteIncomeCategoryById(incomeExpense: IncomeExpense): Int {
        return db.incomeExpenseDao().deleteIncomeExpense(incomeExpense.toIncomeExpenseEntity())
     }

     override fun getIncomeExpenseByCategoryId(categoryId: Long,isIncomeOrExpense:Int): Flow<List<IncomeExpense>> {
         return db.incomeExpenseDao().getIncomeExpenseByCategoryId(categoryId,isIncomeOrExpense).map { incomeExpense ->
             incomeExpense.map { it.toIncomeExpense(productUseCase.color()) }
         }
     }

     override suspend fun deleteByCategoryIdAndIsIncomeOrExpense(categoryIdAndIsIncomeOrExpense: CategoryIdAndIsIncomeOrExpense): Int {
       return   db.incomeExpenseDao().deleteByCategoryId(categoryIdAndIsIncomeOrExpense)
     }


 }
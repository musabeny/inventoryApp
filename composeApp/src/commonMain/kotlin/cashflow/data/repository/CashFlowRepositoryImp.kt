package cashflow.data.repository

import cashflow.data.mapper.toFilterType
import cashflow.data.mapper.toIncomeExpense
import cashflow.data.mapper.toIncomeExpenseEntity
import cashflow.domain.model.FilterType
import cashflow.domain.model.IncomeExpense
import cashflow.domain.repository.CashFlowRepository
import database.InventoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
}
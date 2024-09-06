package cashflow.data.mapper

import cashflow.domain.model.FilterType
import cashflow.domain.model.IncomeExpense
import database.entity.IncomeExpenseEntity
import database.entity.relation.CategoryAndIncomeExpense
import settings.data.mapper.toCategory
import settings.data.mapper.toCategoryWithColor
import settings.domain.model.product.ProductColor

fun CategoryAndIncomeExpense.toIncomeExpense(
    color:List<ProductColor>
):IncomeExpense{
    return IncomeExpense(
        id = incomeExpense.id,
        category = category.toCategory().toCategoryWithColor(color),
        amount = incomeExpense.amount,
        note = incomeExpense.note,
        isIncomeOrExpense = incomeExpense.isIncomeOrExpense,
        dateCreated = incomeExpense.dateCreated
    )
}

fun  IncomeExpense.toIncomeExpenseEntity():IncomeExpenseEntity{
    return IncomeExpenseEntity(
        id = id,
        categoryId = category.id ?: 0L,
        amount = amount,
        note = note,
        isIncomeOrExpense = isIncomeOrExpense,
        dateCreated = dateCreated
    )
}

fun CategoryAndIncomeExpense.toFilterType():FilterType{
  return  FilterType(
        id = category.id ?: 0L,
        label = category.name
    )
}


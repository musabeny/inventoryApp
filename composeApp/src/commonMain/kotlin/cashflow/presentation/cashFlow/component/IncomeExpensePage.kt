package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cashflow.domain.enums.IncomeExpenseType
import cashflow.domain.enums.ListViewType
import cashflow.domain.model.IncomeExpense
import cashflow.presentation.cashFlow.CashFlowEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.expenseWithMinus
import inventoryapp.composeapp.generated.resources.incomeWithPlus
import kotlinx.datetime.LocalDate
import settings.domain.model.category.CategoryWithColor

@Composable
fun IncomeExpensePage(
    categories:List<CategoryWithColor>,
    onEvent: (CashFlowEvent) -> Unit,
    selectedCategory:CategoryWithColor?,
    showIncomeForm:Boolean,
    showCategoryDropDown:Boolean,
    amount:String?,
    note:String?,
    today: String,
    incomeExpenseType: IncomeExpenseType,
    viewType: ListViewType,
    groupedByDate: Map<LocalDate, List<IncomeExpense>>,
    totalIncome:String,
    totalExpense:String,
    navController: NavController,
    incomeExpensesGroup: Map<Pair<Int, CategoryWithColor>, List<IncomeExpense>>,
    bottomSheetForAddCategory:()->Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){
        if(viewType == ListViewType.LIST){
            IncomeExpenseList(
                modifier = Modifier.fillMaxSize(),
                onEvent = onEvent,
                groupedByDate = groupedByDate,
                ){
                HeaderIncomeExpense(
                    modifier = Modifier
                        .fillMaxWidth(),
                    viewType = viewType,
                    onEvent = onEvent,
                    income = totalIncome,
                    expense = totalExpense
                )
            }
        }else{
            IncomeExpenseGroup(
                modifier = Modifier.fillMaxSize(),
                incomeExpensesGroup = incomeExpensesGroup,
                onEvent = onEvent,
                navController = navController
            ){
                HeaderIncomeExpense(
                    modifier = Modifier
                        .fillMaxWidth(),
                    viewType = viewType,
                    onEvent = onEvent,
                    income = totalIncome,
                    expense = totalExpense
                )
            }
        }



        IncomeExpenseButton(
            modifier = Modifier.fillMaxWidth(),
            onEvent = onEvent
        )

     IncomeExpenseForm(
         modifier = Modifier.fillMaxWidth(),
         categories = categories,
         onEvent = onEvent,
         selectedCategory = selectedCategory,
         showIncomeOrExpenseForm = showIncomeForm,
         showCategoryDropDown = showCategoryDropDown,
         bottomSheetForAddCategory = bottomSheetForAddCategory,
         amount = amount,
         note = note,
         today = today,
         incomeExpenseType = incomeExpenseType
     )

    }
}

@Composable
fun IncomeExpenseButton(
    modifier: Modifier,
    onEvent: (CashFlowEvent) -> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CashFlowButton(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.primary,
            label = Res.string.incomeWithPlus
        ) {
            onEvent(CashFlowEvent.ShowIncomeExpenseForm(true,IncomeExpenseType.INCOME))
        }

        CashFlowButton(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.error,
            label = Res.string.expenseWithMinus
        ) {
            onEvent(CashFlowEvent.ShowIncomeExpenseForm(true,IncomeExpenseType.EXPENSE))
        }
    }
}
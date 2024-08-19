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
import cashflow.presentation.cashFlow.CashFlowEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.expenseWithMinus
import inventoryapp.composeapp.generated.resources.incomeWithPlus
import settings.domain.model.category.CategoryWithColor

@Composable
fun IncomeExpensePage(
    categories:List<CategoryWithColor>,
    onEvent: (CashFlowEvent) -> Unit,
    selectedCategory:CategoryWithColor?,
    showIncomeForm:Boolean,
    showCategoryDropDown:Boolean
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){

     Row(
         modifier = Modifier.fillMaxWidth(),
         horizontalArrangement = Arrangement.SpaceBetween
     ) {
        CashFlowButton(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.primary,
            label = Res.string.incomeWithPlus
        ) {
            onEvent(CashFlowEvent.ShowIncomeForm(true))
        }

         CashFlowButton(
             modifier = Modifier,
             color = MaterialTheme.colorScheme.error,
             label = Res.string.expenseWithMinus
         ) {}
     }

     IncomeForm(
         modifier = Modifier.fillMaxWidth(),
         categories = categories,
         onEvent = onEvent,
         selectedCategory = selectedCategory,
         showIncomeForm = showIncomeForm,
         showCategoryDropDown = showCategoryDropDown

     )

    }
}
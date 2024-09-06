package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cashflow.domain.enums.IncomeExpenseType
import cashflow.domain.model.IncomeExpense
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.expense
import inventoryapp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.stringResource
import settings.domain.model.category.CategoryWithColor

@Composable
fun IncomeExpenseGroup(
    modifier: Modifier,
    incomeExpensesGroup: Map<Pair<Int, CategoryWithColor>, List<IncomeExpense>>,
    header:@Composable () -> Unit
){
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
      item(
          span = {
              GridItemSpan(3)
          }
      ) {
          header()
      }

     incomeExpensesGroup.forEach { (category,incomeExpense) ->
       item{
         GroupItem(
             modifier = Modifier,
             category = category.second,
             amount = "${incomeExpense.sumOf { it.amount }}",
             type = if(category.first == IncomeExpenseType.INCOME.value) stringResource(Res.string.income) else stringResource(Res.string.expense)
         )
       }
     }

    }

}


@Composable
fun GroupItem(
    modifier: Modifier,
    category:CategoryWithColor,
    amount:String,
    type: String
){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = category.color.color,
        shadowElevation = 2.dp
    ) {
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp),
           verticalArrangement = Arrangement.spacedBy(6.dp)
       ) {

           GroupItemText(
               text = category.name.lowercase(),
               textAlign = TextAlign.Start
           )

           GroupItemText(
               text = "TSH$amount",
               textAlign = TextAlign.Center,
               style = MaterialTheme.typography.titleMedium,

           )

           GroupItemText(
               text = type.lowercase(),
               textAlign = TextAlign.End
           )


       }
    }
}

@Composable
fun GroupItemText(
    text:String,
    textAlign: TextAlign,
    style:TextStyle = MaterialTheme.typography.bodyMedium
){
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        textAlign = textAlign,
        maxLines = 1,
        style = style.copy(
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    )
}
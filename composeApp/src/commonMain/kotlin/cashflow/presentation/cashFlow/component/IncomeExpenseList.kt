package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cashflow.domain.enums.DaysOfDate
import cashflow.domain.enums.IncomeExpenseType
import cashflow.data.mapper.daysOfDate
import cashflow.data.mapper.formatToMonthYearWithSpace
import cashflow.domain.model.IncomeExpense
import cashflow.presentation.cashFlow.CashFlowEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.today
import inventoryapp.composeapp.generated.resources.yesterday
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import settings.domain.model.category.CategoryWithColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IncomeExpenseList(
    modifier: Modifier,
    onEvent: (CashFlowEvent) -> Unit,
    groupedByDate:Map<LocalDate, List<IncomeExpense>>,

    header:@Composable () -> Unit
){

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            if(groupedByDate.isNotEmpty()){
                header()
            }
        }
        groupedByDate.forEach {(date,datas) ->
          item {
              HeaderIncomeExpenseRow(
                  day = "${date.dayOfMonth}",
                  dayInWord = when(date.daysOfDate()){
                      DaysOfDate.TODAY -> stringResource(Res.string.today)
                      DaysOfDate.YESTERDAY -> stringResource(Res.string.yesterday)
                      else -> null
                  },
                  monthYear = date.formatToMonthYearWithSpace(),
                  expense = "${datas.filter { it.isIncomeOrExpense==IncomeExpenseType.EXPENSE.value }.sumOf { it.amount }   }",
                  income = "${datas.filter { it.isIncomeOrExpense==IncomeExpenseType.INCOME.value }.sumOf { it.amount }   }"
              )
          }
          items(datas.reversed()) {data ->
              ItemIncomeExpense(
                  modifier = Modifier
                      .fillMaxWidth()
                      .combinedClickable(
                         onClick = {},
                          onLongClick = {
                              onEvent(CashFlowEvent.DeleteDialog(true,data))
                          },
                      )
                  ,
                  category = data.category,
                  note = data.note,
                  user = "Musa Beny",
                  expense =if(data.isIncomeOrExpense == IncomeExpenseType.EXPENSE.value) "TSH${data.amount}" else "TSH0",
                  income = if(data.isIncomeOrExpense == IncomeExpenseType.INCOME.value) "TSH${data.amount}" else "TSH0"
              )
          }
        }
    }
}


@Composable
fun HeaderIncomeExpenseRow(
    day:String,
    dayInWord:String?,
    monthYear:String,
    expense:String,
    income:String
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text =day,
                style = MaterialTheme.typography.titleMedium
            )
            Column {
                dayInWord?.let {word ->
                    Text(
                        text = word,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Text(
                    text = monthYear,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        Text(
            modifier = Modifier.weight(1f),
            text = "TSH$expense",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "TSH$income",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun ItemIncomeExpense(
    modifier: Modifier,
    category:CategoryWithColor,
    note:String?,
    user:String,
    expense: String,
    income: String
){

    val localDensity = LocalDensity.current
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    Surface(
      modifier = modifier ,
      shape = RoundedCornerShape(8.dp),
      shadowElevation = 1.dp
    ) {
       Row(
           modifier = Modifier.fillMaxWidth(),
           verticalAlignment = Alignment.CenterVertically
       ) {
         Box(
             modifier = Modifier
                 .width(10.dp)
                 .height(itemHeight)
                 .background(color = category.color.color)
         )
         Column(
             modifier = Modifier
                 .weight(1f)
                 .padding(start = 8.dp)
                 .onGloballyPositioned {
                     itemHeight = with(localDensity){it.size.height.toDp()}
                 }
         ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
             note?.let {
                 Text(
                     text = note,
                     maxLines = 1,
                     style = MaterialTheme.typography.labelMedium.copy(
                         fontWeight = FontWeight.Normal

                     ),
                 )
             }


             Text(
                 text = user,
                 color = MaterialTheme.colorScheme.outline,
                 style = MaterialTheme.typography.labelSmall,
             )
         }

          Box(
              modifier = Modifier
                  .weight(1f)
                  .height(itemHeight)
                  .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f)),
              contentAlignment = Alignment.Center
          ) {
              Text(
                  text = expense,
                  style = MaterialTheme.typography.titleMedium.copy(
                      color = MaterialTheme.colorScheme.error,
                      fontWeight = FontWeight.Bold
                  )
              )
          }


         Text(
             modifier = Modifier
                 .weight(1f),
             text = income,
             textAlign = TextAlign.Center,
             style = MaterialTheme.typography.titleMedium.copy(
                 color = MaterialTheme.colorScheme.primary,
                 fontWeight = FontWeight.Bold
             )

         )
       }
    }
}





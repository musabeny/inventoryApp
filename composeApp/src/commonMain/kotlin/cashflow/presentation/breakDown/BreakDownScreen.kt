package cashflow.presentation.breakDown

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cashflow.data.mapper.daysOfDate
import cashflow.data.mapper.formatToMonthYearWithSpace
import cashflow.domain.enums.DaysOfDate
import cashflow.domain.enums.IncomeExpenseType
import cashflow.presentation.cashFlow.CashFlowEvent
import cashflow.presentation.cashFlow.CashFlowState
import cashflow.presentation.cashFlow.component.DeleteDialog
import cashflow.presentation.cashFlow.component.IncomeExpenseForm
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.today
import inventoryapp.composeapp.generated.resources.yesterday
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BreakDownScreen(
    state: BreakDownState,
    onEvent: (BreakDownEvent) -> Unit,
    categoryId:Long?,
    isIncomeOrExpense:Int?,
    stateCashFlow:CashFlowState,
    onEventCashFlow:(CashFlowEvent) -> Unit,
    navController: NavController
){
    LaunchedEffect(key1 = true){
        onEvent(BreakDownEvent.GetIncomeOrExpense(categoryId,isIncomeOrExpense,onEventCashFlow))
        onEventCashFlow(CashFlowEvent.ShowIncomeExpenseForm(
            true, if(isIncomeOrExpense == IncomeExpenseType.INCOME.value) IncomeExpenseType.INCOME else IncomeExpenseType.EXPENSE))


    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 2.dp)
                    ,
                title = {
                    Text(text = state.category?.name ?: "")
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            onEvent(BreakDownEvent.CloseScreen(navController))
                        },
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    Text(
                        text =  "${state.amount}",
                        color = if(isIncomeOrExpense == IncomeExpenseType.INCOME.value)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )

                }
            )
//            TopBarBreakDown(
//                modifier = Modifier.fillMaxWidth(),
//                itemName = state.category?.name ?: "",
//                amount = "${state.amount}",
//                incomeExpenseType =if(isIncomeOrExpense == IncomeExpenseType.INCOME.value) IncomeExpenseType.INCOME else IncomeExpenseType.EXPENSE,
//                onEvent = onEvent,
//                navController = navController
//            )
        },
        floatingActionButton = {
          FloatingActionButton(
              onClick = {
                  onEventCashFlow(CashFlowEvent.ShowIncomeExpenseForm(
                      true, if(isIncomeOrExpense == IncomeExpenseType.INCOME.value) IncomeExpenseType.INCOME else IncomeExpenseType.EXPENSE))
              }
          ){
              Icon(Icons.Filled.Add, "Floating action button.")
          }
        }
    ){

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .padding(top = it.calculateTopPadding() + 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.incomeExpenses.forEach {(date,data) ->
                item {
                    HeaderBreakDown(
                        modifier = Modifier,
                        day = "${date.dayOfMonth}",
                        dayInWord = when(date.daysOfDate()){
                            DaysOfDate.TODAY -> stringResource(Res.string.today)
                            DaysOfDate.YESTERDAY -> stringResource(Res.string.yesterday)
                            else -> null
                        },
                        monthYear = date.formatToMonthYearWithSpace(),
                        amount = "${data.sumOf { it.amount }   }",
                        incomeExpenseType =if(isIncomeOrExpense == IncomeExpenseType.INCOME.value) IncomeExpenseType.INCOME else IncomeExpenseType.EXPENSE
                    )
                }
                items(data.reversed()){incomeOrExpense ->
                    BreakDownCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {},
                                onLongClick = {
                                    onEventCashFlow(CashFlowEvent.DeleteDialog(true,incomeOrExpense))
                                }
                            ),
                        comment = incomeOrExpense.note ?: "",
                        user = "Musa Beny",
                        amount = "${incomeOrExpense.amount}",
                        incomeExpenseType = if(IncomeExpenseType.INCOME.value == incomeOrExpense.isIncomeOrExpense) IncomeExpenseType.INCOME else IncomeExpenseType.EXPENSE
                    )
                }
            }
        }

        IncomeExpenseForm(
            modifier = Modifier.fillMaxWidth(),
            categories = stateCashFlow.categories,
            onEvent = onEventCashFlow,
            selectedCategory = state.category,
            showIncomeOrExpenseForm = stateCashFlow.showIncomeOrExpenseForm,
            showCategoryDropDown = false,
            bottomSheetForAddCategory = {
            },
            amount = stateCashFlow.amount,
            note = stateCashFlow.note,
            today = stateCashFlow.today,
            incomeExpenseType = if (isIncomeOrExpense == IncomeExpenseType.INCOME.value) IncomeExpenseType.INCOME else IncomeExpenseType.EXPENSE
        )

        DeleteDialog(
            modifier = Modifier,
            showDialog = stateCashFlow.showDeleteDialog,
            onEvent = onEventCashFlow,
            selectedIncomeExpense = stateCashFlow.selectedIncomeExpense,
            type = stateCashFlow.viewType
        )
    }


}



@Composable
fun HeaderBreakDown(
    modifier: Modifier,
    day:String,
    dayInWord:String?,
    monthYear:String,
    amount:String,
    incomeExpenseType: IncomeExpenseType
){
    Row(
        modifier = modifier.padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text =day,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
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
            modifier = Modifier,
            text = "TSH$amount",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = if(incomeExpenseType == IncomeExpenseType.INCOME)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            ),
            textAlign = TextAlign.Center
        )
    }

}


@Composable
fun TopBarBreakDown(
    modifier: Modifier,
    itemName:String,
    amount:String,
    incomeExpenseType: IncomeExpenseType,
    onEvent: (BreakDownEvent) -> Unit,
    navController: NavController
){
    Surface(
        modifier = modifier,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier.clickable {
                    onEvent(BreakDownEvent.CloseScreen(navController))
                },
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = itemName
            )
            Text(
                text = amount,
                color = if(incomeExpenseType == IncomeExpenseType.INCOME)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun BreakDownCard(
    modifier: Modifier,
    comment:String,
    user:String,
    amount: String,
    incomeExpenseType: IncomeExpenseType
){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp
    ) {
      Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp)
              .padding(vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = comment,
                style = MaterialTheme.typography.titleSmall.copy(
                   fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = user,
                style = MaterialTheme.typography.titleSmall.copy(
                   color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                )
            )
        }
        Text(
            text = amount,
            style = MaterialTheme.typography.titleSmall.copy(
                color = if(incomeExpenseType == IncomeExpenseType.INCOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        )
      }
    }
}
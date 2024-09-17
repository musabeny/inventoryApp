package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cashflow.domain.enums.ListViewType
import cashflow.presentation.cashFlow.CashFlowEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.expense
import inventoryapp.composeapp.generated.resources.filter
import inventoryapp.composeapp.generated.resources.grid
import inventoryapp.composeapp.generated.resources.group
import inventoryapp.composeapp.generated.resources.income
import inventoryapp.composeapp.generated.resources.list
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HeaderIncomeExpense(
    modifier: Modifier,
    viewType: ListViewType,
    onEvent: (CashFlowEvent) -> Unit,
    expense: String,
    income: String

){
    val localDensity = LocalDensity.current
    var dividerHeight by remember {
        mutableStateOf(0.dp)
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
                .onGloballyPositioned {
                    dividerHeight = with(localDensity){it.size.height.toDp()}
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ViewOptionGroup(
                modifier = Modifier.weight(1f),
                viewType = viewType,
                onEvent = onEvent
            )
            VerticalDivider(
                modifier = Modifier.height(dividerHeight),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outline
            )
            ExpenseIncomeGroupLabel(
                modifier = Modifier.weight(1f),
                income = income,
                expense = expense
            )
        }
    }

}


@Composable
fun ViewOptionGroup(
    modifier: Modifier,
    viewType: ListViewType,
    onEvent: (CashFlowEvent) -> Unit,

    ){
    Row(
        modifier = modifier
    ) {
        ViewOption(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onEvent(CashFlowEvent.ChangeViewType(ListViewType.LIST))
                }
                .background(
                    color = if(viewType == ListViewType.LIST) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                ),
            icon = Icons.AutoMirrored.Filled.List,
            label = Res.string.list
        )

        ViewOption(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onEvent(CashFlowEvent.ChangeViewType(ListViewType.GROUP))
                }
                .background(
                    color = if(viewType == ListViewType.GROUP) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                ),
            iconDra = Res.drawable.grid,
            label = Res.string.group
        )
        ViewOption(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onEvent(CashFlowEvent.ShowFilterSheet(true))
                },
            iconDra = Res.drawable.filter,
            label = Res.string.filter,
            isFilter = true
        )
    }
}

@Composable
fun ViewOption(
    modifier: Modifier,
    icon: ImageVector? = null,
    iconDra: DrawableResource? = null,
    label: StringResource,
    isFilter:Boolean = false
){
    Column(
        modifier = modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.fillMaxWidth(),
                imageVector = it ,
                contentDescription = "Icons",
                tint = MaterialTheme.colorScheme.surface
            )
        }
        iconDra?.let {
            Icon(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(it),
                contentDescription = "Icons",
                tint =if(isFilter)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            )
        }

        Text(
            text = stringResource(label),
            color =if (isFilter)MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.titleMedium
        )

    }
}

@Composable
fun ExpenseIncomeGroupLabel(
    modifier: Modifier,
    expense: String,
    income: String
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExpenseIncomeLabel(
            modifier = Modifier.weight(1f),
            amount = "TSH$expense",
            label = Res.string.expense,
            isExpense = true
        )

        ExpenseIncomeLabel(
            modifier = Modifier.weight(1f),
            amount = "TSH$income",
            label = Res.string.income
        )
    }
}


@Composable
fun ExpenseIncomeLabel(
    modifier: Modifier,
    amount:String,
    label: StringResource,
    isExpense:Boolean = false
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = amount,
            color = if(isExpense)MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}
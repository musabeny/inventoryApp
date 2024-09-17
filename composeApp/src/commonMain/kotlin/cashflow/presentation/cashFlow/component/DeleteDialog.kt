package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cashflow.domain.enums.ListViewType
import cashflow.domain.model.IncomeExpense
import cashflow.presentation.cashFlow.CashFlowEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.are_you_sure_you_want_to_delete_category_and_transaction
import inventoryapp.composeapp.generated.resources.are_you_sure_you_want_to_delete_item
import inventoryapp.composeapp.generated.resources.no
import inventoryapp.composeapp.generated.resources.yes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    modifier: Modifier,
    showDialog:Boolean,
    onEvent: (CashFlowEvent) -> Unit,
    selectedIncomeExpense:IncomeExpense?,
    type:ListViewType
){
    if(showDialog){
        BasicAlertDialog(
            onDismissRequest = {
                onEvent(CashFlowEvent.DeleteDialog(false,null))
            },
            modifier = modifier
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    ,
                border = BorderStroke(width = 1.dp, color = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(if(type == ListViewType.LIST) Res.string.are_you_sure_you_want_to_delete_item else Res.string.are_you_sure_you_want_to_delete_category_and_transaction),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DialogButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if(type == ListViewType.GROUP){
                                    selectedIncomeExpense?.let {
                                        onEvent(CashFlowEvent.DeleteCategoryWithItems(it.category.id ?: 0L,it.isIncomeOrExpense))
                                    }
                                }else{
                                    onEvent(CashFlowEvent.DeleteIncomeExpense(selectedIncomeExpense))
                                }

                            },
                            containerColor = MaterialTheme.colorScheme.error
                        )
                        DialogButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onEvent(CashFlowEvent.DeleteDialog(false,null))
                            },
                            label = Res.string.no
                        )
                    }
                }
            }

        }
    }

}

@Composable
fun DialogButton(
    modifier: Modifier,
    onClick:() -> Unit,
    containerColor:Color = MaterialTheme.colorScheme.primary,
    label:StringResource = Res.string.yes
){
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onPrimary

        )
    ){
      Text(text = stringResource(label))
    }
}
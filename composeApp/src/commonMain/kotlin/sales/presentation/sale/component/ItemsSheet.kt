package sales.presentation.sale.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import sales.domain.model.ItemDetail
import sales.presentation.sale.SaleEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsSheet(
    modifier: Modifier,
    items:List<ItemDetail>,
    showSheet:Boolean,
    onEvent: (SaleEvent) -> Unit
){
    if(showSheet){
        ModalBottomSheet(
            modifier = modifier,
            shape = RoundedCornerShape(2.dp),
            dragHandle = null,
            onDismissRequest = {
                onEvent(SaleEvent.ShowSheet(false))
            }
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    modifier = Modifier.clickable {
                        onEvent(SaleEvent.ShowSheet(false))
                    },
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Icon"
                )
                items.forEach { item ->
                    ItemRow(
                        modifier = Modifier.fillMaxWidth(),
                        itemName = item.product ?: "",
                        total =if(item.total == null) "" else "${item.total}",
                        expression = item.expression ?: ""
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = DividerDefaults.color.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }

}

@Composable
fun ItemRow(
    modifier: Modifier,
    itemName:String,
    total:String,
    expression:String
){
    Row(
        modifier = modifier
    ) {
     Column(
         modifier = Modifier
     ) {
         Text(
             text = itemName,
             style = MaterialTheme.typography.titleSmall.copy(
                 fontWeight = FontWeight.Bold
             )
         )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = expression,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.outline
                )
            )

            Text(
                text = total,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.outline
                )
            )
        }
     }
    }
}
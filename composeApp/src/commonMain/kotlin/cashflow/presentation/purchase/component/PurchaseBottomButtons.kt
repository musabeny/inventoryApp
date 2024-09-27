package cashflow.presentation.purchase.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cashflow.presentation.purchase.PurchaseEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.create_purchase
import inventoryapp.composeapp.generated.resources.save_draft

@Composable
fun PurchaseBottomButtons(
    modifier: Modifier,
    onEvent: (PurchaseEvent) -> Unit
){
    Row(
        modifier = modifier
    ) {
        PurchaseButton(
            modifier = Modifier
                .weight(1.5f)
                .padding(8.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            label = Res.string.create_purchase
        ){
            onEvent(PurchaseEvent.SavePurchase)
        }
        PurchaseButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            containerColor = MaterialTheme.colorScheme.secondary,
            textColor = MaterialTheme.colorScheme.onSecondary,
            label = Res.string.save_draft
        ){
        }
    }
}
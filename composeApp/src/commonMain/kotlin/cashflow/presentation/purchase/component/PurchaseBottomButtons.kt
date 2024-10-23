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
import inventoryapp.composeapp.generated.resources.update_purchase

@Composable
fun PurchaseBottomButtons(
    modifier: Modifier,
    onEvent: (PurchaseEvent) -> Unit,
    billId:Long? = null
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
            label = if(billId == null) Res.string.create_purchase else Res.string.update_purchase
        ){
            onEvent(PurchaseEvent.SavePurchase(isDraft = false))
        }
        PurchaseButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            containerColor = MaterialTheme.colorScheme.secondary,
            textColor = MaterialTheme.colorScheme.onSecondary,
            label = Res.string.save_draft
        ){
            onEvent(PurchaseEvent.SavePurchase(isDraft = true))
        }
    }
}
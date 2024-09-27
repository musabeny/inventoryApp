package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import cashflow.presentation.cashFlow.CashFlowEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_purchase
import org.jetbrains.compose.resources.stringResource

@Composable
fun PurchasePage(
    onEvent: (CashFlowEvent) -> Unit,
    navController: NavController
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
       Button(
           onClick = {
              onEvent(CashFlowEvent.GoToPurchase(navController))
           }
       ){
           Text(
               text = stringResource(Res.string.add_purchase),
               fontWeight = FontWeight.Bold
           )
       }
    }
}
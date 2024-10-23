package cashflow.presentation.purchase.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cashflow.domain.model.purchase.PurchaseItem
import cashflow.presentation.purchase.PurchaseEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.itemName
import inventoryapp.composeapp.generated.resources.price

@Composable
fun PurchaseItemView(
    item: PurchaseItem,
    index:Int,
    onEvent: (PurchaseEvent) -> Unit
){

    val localDensity = LocalDensity.current
    var tabHeight by remember {
        mutableStateOf(0.dp)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned {coordinate ->
                    tabHeight = with(localDensity){coordinate.size.height.toDp()}
                },
            shape = RoundedCornerShape(2.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                CustomBasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .weight(3f),
                    value = item.name,
                    onValueChange = {
                        onEvent(PurchaseEvent.EnterItemName(it,index))
                    },
                    label = Res.string.itemName,
                )
                VerticalDivider(
                    modifier = Modifier.height(tabHeight),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                )
                CustomBasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .weight(1f),
                    value = item.price,
                    onValueChange = {
                        onEvent(PurchaseEvent.EnterPrice(it,index))
                    },
                    label = Res.string.price,
                    keyboardType = KeyboardType.Number
                )


            }

        }

        Box(
            modifier = Modifier
                .size(tabHeight* 0.7f)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.error,
                )
                .clickable {
                    onEvent(PurchaseEvent.ShowDialog(true,index,item))
                },
            contentAlignment = Alignment.Center
        ) {
            HorizontalDivider(
                modifier = Modifier.width(tabHeight * 0.4f),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onError
            )
        }




    }
}
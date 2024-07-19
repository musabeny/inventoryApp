package settings.presentation.product.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.expiry_date
import inventoryapp.composeapp.generated.resources.expiry_date_alert
import settings.presentation.product.ProductEvent

@Composable
fun TrackExpiry(
    modifier: Modifier,
    onEvent: (ProductEvent) -> Unit,
    selectedDate:String? = null,
    value:String  = ""
){
    var boxHeight by remember {
        mutableStateOf(0.dp)
    }
    val localDensity = LocalDensity.current
    Surface(
        modifier = modifier,
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Track Entry",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DateBox(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(boxHeight)
                        .clickable {
                            onEvent(ProductEvent.DatePickerDialog(true))
                        }

                    ,
                    label = Res.string.expiry_date,
                    selectedDate = selectedDate,
                    onEvent = onEvent
                )
                DateBox(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .onGloballyPositioned {
                            boxHeight = with(localDensity){it.size.height.toDp()}
                        }
                    ,
                    label = Res.string.expiry_date_alert,
                    isExpireDate = false,
                    onEvent = onEvent,
                    value = value
                )
            }
        }

    }
}
package sales.presentation.payment.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.component.CustomIcon
import sales.presentation.payment.PaymentEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentTopBar(
   modifier: Modifier,
   selectedDate:String,
   total:String,
   onEvent: (PaymentEvent) -> Unit
){
    Surface(
        modifier = Modifier,
        shadowElevation = 2.dp
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Row(
                    modifier = Modifier.clickable {
                        onEvent(PaymentEvent.ShowDateDialog(true))
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CustomIcon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Filled.DateRange
                    )
                    Text(
                        text = selectedDate,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    CustomIcon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Filled.ArrowDropDown,
                    )
                }
            },
            navigationIcon = {
                CustomIcon(
                    modifier = Modifier.clickable {
                        onEvent(PaymentEvent.NavigateBack)
                    },
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack
                )
            },
            actions = {
                Text(
                    text = total,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        )
    }

}
package cashflow.presentation.purchase.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cashflow.presentation.purchase.PurchaseEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.note

@Composable
fun NoteAndDate(
    modifier: Modifier,
    onEvent: (PurchaseEvent) -> Unit,
    note:String,
    selectedDate:String
){
    val localDensity = LocalDensity.current
    var tabHeight by remember {
        mutableStateOf(0.dp)
    }
    Row(
        modifier =modifier  ,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InputAndTitle(
            modifier = Modifier
                .weight(3f)
                .onGloballyPositioned {coordinate ->
                    tabHeight = with(localDensity){coordinate.size.height.toDp()}
                },
            label = Res.string.note,
            value = note
        ){
            onEvent(PurchaseEvent.EnterNote(it))
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(tabHeight)
                .background(color = MaterialTheme.colorScheme.primary)
                .clickable {
                    onEvent(PurchaseEvent.ShowDatePicker(true))
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = selectedDate,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}
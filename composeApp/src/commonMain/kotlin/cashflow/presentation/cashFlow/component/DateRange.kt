package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cashflow.presentation.cashFlow.CashFlowEvent
import core.component.CustomIcon

@Composable
fun DateRange(
    modifier: Modifier,
    dateRange:String,
    onEvent: (CashFlowEvent) -> Unit,
    navController: NavController,
    showNextArrow:Boolean
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CustomIcon(
            modifier = Modifier.clickable {
                onEvent(CashFlowEvent.Back)
            },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onEvent(CashFlowEvent.GoToDateSelection(navController))
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomIcon(
                modifier = Modifier.size(38.dp),
                imageVector = Icons.Filled.DateRange
            )
            Spacer(Modifier.width(8.dp))

            Text(
                text = dateRange,
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        }



        CustomIcon(
            modifier = Modifier
                .alpha(if(showNextArrow)1f else 0f)
                .clickable(enabled = showNextArrow) {

                onEvent(CashFlowEvent.Next)
            }
            ,
            imageVector = Icons.AutoMirrored.Filled.ArrowForward
        )

    }
}


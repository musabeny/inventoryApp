package cashflow.presentation.customCalender.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cashflow.presentation.customCalender.CalenderEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.last_week
import inventoryapp.composeapp.generated.resources.this_week
import inventoryapp.composeapp.generated.resources.today
import inventoryapp.composeapp.generated.resources.yesterday
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateButtons(
   modifier: Modifier,
   onEvent: (CalenderEvent) -> Unit,
   currentMonth:String,
   currentYear:String,
   navController: NavController
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
        DateButton(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.today),
            onClick = {
                onEvent(CalenderEvent.ToDay(navController ))
            }
        )
            DateButton(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.yesterday),
                onClick = {
                    onEvent(CalenderEvent.Yesterday(navController ))
                }
            )
            DateButton(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.this_week),
                onClick = {
                    onEvent(CalenderEvent.ThisWeek(navController ))
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DateButton(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.last_week),
                onClick = {
                    onEvent(CalenderEvent.LastWeek(navController ))
                }
            )
            DateButton(
                modifier = Modifier.weight(1f),
                label = currentMonth,
                onClick = {
                    onEvent(CalenderEvent.ThisMonth(navController ))
                }
            )
            DateButton(
                modifier = Modifier.weight(1f),
                label = currentYear,
                onClick = {
                    onEvent(CalenderEvent.ThisYear(navController ))
                }
            )
        }
    }
}

@Composable
fun DateButton(
    modifier: Modifier,
    onClick:() -> Unit,
    label:String
){
    Button(
     modifier = modifier,
     onClick = onClick ,
    ){
      Text(
          text = label,
          color = MaterialTheme.colorScheme.onPrimary
      )
    }
}
package cashflow.presentation.customCalender

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cashflow.domain.mapper.formatToMonthYear
import cashflow.domain.model.FullDate
import cashflow.presentation.customCalender.component.CalenderView
import cashflow.presentation.customCalender.component.DateButtons
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.choose
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun CalenderScreen(
    state: CalenderState,
    onEvent: (CalenderEvent)-> Unit,
    navController: NavController
){
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = true){
        coroutineScope.launch {
            lazyColumnState.animateScrollToItem(index = Int.MAX_VALUE)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DateButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp),
                onEvent = onEvent,
                currentMonth = state.currentMonth,
                currentYear = state.currentYear,
                navController = navController
            )
            CalenderView(
                modifier = Modifier.fillMaxSize(),
                calender = state.calender,
                lazyColumnState = lazyColumnState,
                daysOfWeeks = state.daysOfWeeks,
                onEvent = onEvent
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                onEvent(CalenderEvent.DateSelected(navController))
            }
        ){
           Text(
               text = stringResource(Res.string.choose),
               fontWeight = FontWeight.SemiBold
           )
        }
    }
}

@Composable
fun ContentItem(
    modifier: Modifier,
    label: FullDate
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
       Box(
           modifier=Modifier
               .fillMaxSize()
               .background(
                   color = if(label.isSelected)MaterialTheme.colorScheme.primary else Color.Transparent,
               )
               ,
           contentAlignment = Alignment.Center
       ){
           Text(
               modifier = Modifier.padding(8.dp),
               text = "${label.day?.dayOfMonth ?: ""}",
               style = MaterialTheme.typography.bodyMedium,
               textAlign = TextAlign.Center,
               color = if(label.isSelected) MaterialTheme.colorScheme.onPrimary else Color.Black
           )
       }

    }

}

@Composable
fun DaysWeekItem(
    modifier: Modifier,
    label:String
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )

    }

}
package cashflow.presentation.customCalender.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cashflow.data.mapper.formatToMonthYear
import cashflow.domain.model.CashFlowDate.CustomDate
import cashflow.domain.model.CashFlowDate.FullDate
import cashflow.presentation.customCalender.CalenderEvent
import cashflow.presentation.customCalender.ContentItem
import cashflow.presentation.customCalender.DaysWeekItem

@Composable
fun CalenderView(
    modifier: Modifier,
    calender: List<CustomDate>,
    lazyColumnState: LazyListState,
    daysOfWeeks: List<String>,
    onEvent: (CalenderEvent) -> Unit
){
    LazyColumn(
        modifier = modifier,
        state = lazyColumnState
    ) {
        items(calender){calender ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var index = 0
                Text(
                    text = if(calender.fullDate[8].day == null)"" else calender.fullDate[8].day?.formatToMonthYear() ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    repeat(daysOfWeeks.size){index ->
                        DaysWeekItem(
                            modifier = Modifier.weight(1f),
                            label =  daysOfWeeks[index]
                        )
                    }
                }
                repeat(6){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(7){
                            val item = if (index < calender.fullDate.size) calender.fullDate[index] else FullDate()
                            ContentItem(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(color = if(item.isCurrentDate) Color.Yellow else Color.Transparent)
                                    .alpha(if(item.isActive) 1.0f else 0.4f)
                                    .clickable(enabled = item.isActive) {
                                        onEvent(CalenderEvent.SelectDate(item,index))
                                    },
                                label = item
                            )
                            index++
                        }
                    }
                }
            }
        }
    }
}
package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cashflow.domain.enums.UserFilterType
import cashflow.domain.model.FilterType
import cashflow.presentation.cashFlow.CashFlowEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.clear_all
import inventoryapp.composeapp.generated.resources.entry_type
import inventoryapp.composeapp.generated.resources.expense_category
import inventoryapp.composeapp.generated.resources.filter
import inventoryapp.composeapp.generated.resources.income_category
import inventoryapp.composeapp.generated.resources.members
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    modifier: Modifier,
    showSheet:Boolean,
    onEvent: (CashFlowEvent)-> Unit,
    filters: List<FilterType>
){
    if(showSheet){
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = {
                onEvent(CashFlowEvent.ShowFilterSheet(false))
            },
            dragHandle = null,
            shape = RoundedCornerShape(2.dp)
        ){
         Column(
             modifier = Modifier.fillMaxWidth(),
             verticalArrangement = Arrangement.Top
         ) {
             HeaderFilter(
                 modifier = Modifier.fillMaxWidth(),
                 onEvent = onEvent
             )
             BodyFilter(
                 modifier = Modifier.fillMaxWidth(),
                 onEvent = onEvent,
                 filters = filters
             )

         }
        }
    }

}

@Composable
fun HeaderFilter(
    modifier: Modifier,
    onEvent: (CashFlowEvent) -> Unit
){
  Column(
      modifier = modifier
  ) {
      Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically
      ) {
          Row(
              modifier = Modifier
                  .weight(1f),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
              Icon(
                  modifier = Modifier.clickable {
                      onEvent(CashFlowEvent.ShowFilterSheet(false))
                  },
                  imageVector = Icons.Filled.Close,
                  contentDescription = "Close",
                  tint = MaterialTheme.colorScheme.primary
              )
              TextFilter(
                  text = Res.string.filter,
                  fontWeight = FontWeight.SemiBold
              )
          }

          TextFilter(
              modifier = Modifier.clickable {
                  onEvent(CashFlowEvent.ClearAllFilter)
              },
              text = Res.string.clear_all,
              color = MaterialTheme.colorScheme.error,
              fontWeight = FontWeight.SemiBold
          )

      }
      HorizontalDivider(
          modifier = Modifier.fillMaxWidth(),
          color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
          thickness = 1.dp
      )
  }

}

@Composable
fun BodyFilter(
    modifier: Modifier,
    onEvent: (CashFlowEvent) -> Unit,
    filters: List<FilterType>
){
    Row(
        modifier = modifier
    ) {
        LeftBodyFilter(modifier = Modifier
            .weight(1f)
            .padding(vertical = 8.dp)
            .padding(start = 8.dp),
            onEvent = onEvent
        )
        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )
        RightBodyFilter(
            modifier = Modifier.weight(1f),
            filters = filters,
            onEvent = onEvent
        )
    }
}

@Composable
fun RightBodyFilter(
    modifier: Modifier,
    filters:List<FilterType>,
    onEvent: (CashFlowEvent) -> Unit
){
   LazyColumn(
       modifier = modifier
   ) {
      items(filters){filter ->
          Row(
             modifier = Modifier.fillMaxWidth() ,
             verticalAlignment = Alignment.CenterVertically
          ) {
              Checkbox(
                  checked = filter.isChecked,
                  onCheckedChange = {
                     onEvent(CashFlowEvent.SelectedFilter(filter, it ))
                  }
              )
              Text(text = filter.label)
          }
      }
   }
}

@Composable
fun LeftBodyFilter(
    modifier: Modifier,
    onEvent: (CashFlowEvent) -> Unit
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      LeftBodyFilterItem(
          modifier = Modifier
              .fillMaxWidth()
              .clickable {
                  onEvent(CashFlowEvent.SelectedFilterType(UserFilterType.ENTRY))
              },
          text = Res.string.entry_type
      )
        LeftBodyFilterItem(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                },
            text = Res.string.members
        )
        LeftBodyFilterItem(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onEvent(CashFlowEvent.SelectedFilterType(UserFilterType.INCOME))
                },
            text = Res.string.income_category
        )
        LeftBodyFilterItem(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onEvent(CashFlowEvent.SelectedFilterType(UserFilterType.EXPENSE))
                },
            text = Res.string.expense_category
        )
    }
}

@Composable
fun LeftBodyFilterItem(
    modifier: Modifier,
    text: StringResource
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      TextFilter(
          text = text,
          modifier = Modifier.fillMaxWidth()
      )
      HorizontalDivider(
          modifier = Modifier.fillMaxWidth(),
          color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
          thickness = 1.dp
      )
    }
}

@Composable
fun TextFilter(
    modifier: Modifier = Modifier,
    text:StringResource,
    fontWeight:FontWeight = FontWeight.Normal,
    color:Color = Color.Black,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = MaterialTheme.typography.titleMedium
){
    Text(
        modifier = modifier,
        text = stringResource(text),
        style = style.copy(
            fontWeight =fontWeight,
            color =color,
            textAlign = textAlign
        ),
        maxLines = 1
    )
}
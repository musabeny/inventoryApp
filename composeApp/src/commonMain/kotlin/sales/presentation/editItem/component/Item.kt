package sales.presentation.editItem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.name
import inventoryapp.composeapp.generated.resources.price
import inventoryapp.composeapp.generated.resources.quantity
import org.jetbrains.compose.resources.stringResource
import sales.presentation.editItem.EditItemEvent

@Composable
fun Item(
    itemName:String,
    price:String,
    quantity:String,
    index:Int,
    onEvent: (EditItemEvent) -> Unit
){
    val localDensity = LocalDensity.current
    // Create element height in dp state
    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
        ,
        shape = RoundedCornerShape(8.dp)
    ) {
      Column(
          modifier = Modifier
              .fillMaxWidth()
              .padding(8.dp)
      ) {
          Row(
              modifier = Modifier
                  .fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)

          ) {
              Column(
                  modifier = Modifier
                      .weight(4f)
                      .height(columnHeightDp)
                      ,
                  verticalArrangement = Arrangement.SpaceBetween
              ) {
                  Text(
                      text = stringResource(Res.string.name),
                      style = MaterialTheme.typography.labelSmall.copy(
                          color = MaterialTheme.colorScheme.outline
                      )
                  )
                  Column(
                      verticalArrangement = Arrangement.Bottom
                  ) {
                      Text(
                          text = itemName,
                          style = MaterialTheme.typography.bodyMedium
                      )
                      HorizontalDivider(
                          color = MaterialTheme.colorScheme.outline
                      )
                  }

              }
              Column(
                  modifier = Modifier
                      .weight(1f)
                      .height(columnHeightDp),
                  verticalArrangement = Arrangement.SpaceBetween,
                  horizontalAlignment = Alignment.CenterHorizontally
              ) {
                  Text(
                      text = stringResource(Res.string.price),
                      style = MaterialTheme.typography.labelSmall.copy(
                          color = MaterialTheme.colorScheme.outline,
                      ),

                  )
                  Column(
                      verticalArrangement = Arrangement.Bottom,
                      horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                      Text(
                          text = price,
                          style = MaterialTheme.typography.labelSmall.copy(
                              color = MaterialTheme.colorScheme.outline
                          )
                      )
                      HorizontalDivider(
                          color = MaterialTheme.colorScheme.outline
                      )
                  }

              }
              VerticalDivider(
                  modifier = Modifier.height(columnHeightDp),
                  color = MaterialTheme.colorScheme.outline
              )
              Column(
                  modifier = Modifier
                      .weight(2f)
                      .onGloballyPositioned { coordinates ->
                          columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                      }
                     ,
                  verticalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                  Text(
                      modifier = Modifier.fillMaxWidth(),
                      text = stringResource(Res.string.quantity),
                      style = MaterialTheme.typography.labelSmall.copy(
                          color = MaterialTheme.colorScheme.outline,
                          textAlign = TextAlign.Center
                      )
                  )
                  Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                      Text(
                          modifier = Modifier
                              .weight(1f)
                              .clickable {
                                onEvent(EditItemEvent.UpdateQuantity(-1,index))
                              },
                          text = "-",
                          style = MaterialTheme.typography.titleLarge.copy(
                              color = MaterialTheme.colorScheme.primary,
                              fontWeight = FontWeight.SemiBold,
                              textAlign = TextAlign.Start
                          )
                      )
                      Column(
                          modifier = Modifier.weight(1f),
                          verticalArrangement = Arrangement.Bottom,
                          horizontalAlignment = Alignment.CenterHorizontally
                      ) {

                          BasicTextField(
                              modifier = Modifier.fillMaxWidth(),
                              onValueChange = {
                                  onEvent(EditItemEvent.UpdateQuantity(
                                      quantity = it.toIntOrNull() ?: 0,
                                      index = index,
                                      isEditText = true
                                  ))
                              },
                              value = quantity,
                              keyboardOptions = KeyboardOptions.Default.copy(
                                  keyboardType = KeyboardType.Number
                              ),
                              textStyle = TextStyle.Default.copy(
                                  textAlign = TextAlign.Center
                              )
                          )
                          HorizontalDivider()
                      }
                      Text(
                          modifier = Modifier
                              .weight(1f)
                              .clickable {
                                  onEvent(EditItemEvent.UpdateQuantity(1,index))
                              },
                          text = "+",
                          style = MaterialTheme.typography.titleLarge.copy(
                              color = MaterialTheme.colorScheme.primary,
                              textAlign = TextAlign.End,
                              fontWeight = FontWeight.SemiBold,
                          )
                      )
                  }
              }


          }
          Box(
              modifier = Modifier
                  .fillMaxWidth()
                  .height(16.dp)
          )
      }


    }
}
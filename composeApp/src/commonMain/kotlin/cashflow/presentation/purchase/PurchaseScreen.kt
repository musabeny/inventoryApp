package cashflow.presentation.purchase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cashflow.data.mapper.toBillItem
import cashflow.presentation.purchase.component.InputAndTitle
import cashflow.presentation.purchase.component.NoteAndDate
import cashflow.presentation.purchase.component.PurchaseBottomButtons
import cashflow.presentation.purchase.component.PurchaseButton
import cashflow.presentation.purchase.component.PurchaseItemView
import core.component.SelectDate
import core.component.StandardDialog
import core.util.UiEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.bill_title
import inventoryapp.composeapp.generated.resources.new_purchase
import inventoryapp.composeapp.generated.resources.remove_this_item
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseScreen(
    state: PurchaseState,
    onEvent: (PurchaseEvent) -> Unit,
    navController: NavController,
    snackBarHost: SnackbarHostState,
    uiEvent: Flow<UiEvent>,
    billId: Long?
){
    val scrollState  = rememberScrollState()
    LaunchedEffect(true){
        billId?.let {
            onEvent(PurchaseEvent.GetPurchaseById(billId))
        }

        uiEvent.collect{event ->
            when(event){
                is UiEvent.Navigate -> {}
                is UiEvent.PopBackStack -> {
                    navController.navigateUp()
                }
                is UiEvent.ShowSnackBar -> {
                    snackBarHost.showSnackbar(event.message.asString())
                }
            }
        }
    }
    Scaffold(
       topBar = {
           CenterAlignedTopAppBar(
               modifier = Modifier
                   .fillMaxWidth()
                   .shadow(elevation = 2.dp),
               colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                   containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
               ),
               title = {
                 Text(
                     text = stringResource(Res.string.new_purchase),
                     fontWeight = FontWeight.Normal,
                     color = MaterialTheme.colorScheme.onPrimary
                 )
               },
               actions = {
                   Icon(
                       modifier = Modifier.clickable {
                           onEvent(PurchaseEvent.GoBack)
                       },
                       imageVector = Icons.Filled.Close,
                       contentDescription = "Close Icon",
                       tint = MaterialTheme.colorScheme.onPrimary
                   )
               }
           )
       }
    ) {
       Box (
           modifier = Modifier
               .fillMaxSize(),
           contentAlignment = Alignment.BottomCenter
       ){
           Column(
               modifier = Modifier
                   .fillMaxSize()
                   .padding(top = it.calculateTopPadding())
                   .padding( 8.dp)
                   .verticalScroll(scrollState),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.spacedBy(8.dp)
           ){
               InputAndTitle(
                   modifier = Modifier.fillMaxWidth(),
                   label = Res.string.bill_title,
                   value = state.billTitle,
                   error = state.billError?.asUiString()
               ){
                   onEvent(PurchaseEvent.EnterBillTitle(it))
               }

               NoteAndDate(
                   modifier = Modifier.fillMaxWidth(),
                   note = state.note,
                   onEvent = onEvent,
                   selectedDate = state.selectedDateInString
               )

               Column(
                   modifier = Modifier
                       .fillMaxWidth(),
                   verticalArrangement = Arrangement.spacedBy(8.dp)
               ) {
                   state.items.forEachIndexed { index, purchaseItem ->
                       PurchaseItemView(
                           item = purchaseItem,
                           index = index,
                           onEvent = onEvent
                       )
                   }


               }

               PurchaseButton{
                   onEvent(PurchaseEvent.AddPurchaseItem)
               }

               SelectDate(
                   modifier = Modifier,
                   showDatePickerDialog = state.showDatePicker,
                   onDismiss = {
                       onEvent(PurchaseEvent.ShowDatePicker(show = false))
                   }
               ){

                   onEvent(PurchaseEvent.SelectedDate(it.selectedDateMillis))

               }

           }

           PurchaseBottomButtons(
               modifier = Modifier.fillMaxWidth(),
               onEvent = onEvent,
               billId = billId
           )

           StandardDialog(
               modifier = Modifier,
               message = stringResource(Res.string.remove_this_item),
               onConfirm = {
                   if(state.selectedIndex != null ){
                       onEvent(PurchaseEvent.RemovePurchaseItem(state.selectedIndex,state.selectedItem?.toBillItem()))

                   }
               },
               onCancel = {
                   onEvent(PurchaseEvent.ShowDialog(false))
               },
               showDialog = state.showDialog
           )
       }

    }
}









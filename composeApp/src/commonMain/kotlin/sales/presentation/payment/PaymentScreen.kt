package sales.presentation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import core.component.SelectDate
import core.util.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import sales.domain.model.ItemDetail
import sales.presentation.payment.component.ItemsSection
import sales.presentation.payment.component.PaymentTopBar
import sales.presentation.payment.component.TotalSections
import settings.data.mapper.toLocalDate
import settings.data.mapper.toLocalDateWithMonthName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    state: PaymentState,
    onEvent: (PaymentEvent) -> Unit,
    uiEvent: Flow<UiEvent>,
    items:List<ItemDetail>,
    navController: NavController,
    snackBarHost: SnackbarHostState,
    saveItems:(List<ItemDetail>)-> Unit
){

    LaunchedEffect(key1 = true){
        onEvent(PaymentEvent.InitData(items))

        uiEvent.collect{event ->
            when(event){
                is  UiEvent.Navigate ->{
                    navController.navigate(event.route)
                }
                is UiEvent.PopBackStack -> {
                    navController.navigateUp()
                }
                is UiEvent.ShowSnackBar -> {
                    snackBarHost.showSnackbar(
                        message = event.message.asString(),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

   Scaffold(
       modifier = Modifier.fillMaxSize(),
       topBar = {
         PaymentTopBar(
             modifier = Modifier
                 .fillMaxWidth()
                 ,
             selectedDate =state.selectedDate,
             total = state.total,
             onEvent = onEvent
         )
       }
   ) {
      Column(modifier = Modifier
          .fillMaxSize()
          .background(color = MaterialTheme.colorScheme.surfaceContainer)
          .padding(top = it.calculateTopPadding())
      ) {
          ItemsSection(
              modifier = Modifier
                  .fillMaxWidth()
                  .fillMaxHeight(0.5f)
                  .padding(top = 8.dp)
                  .clickable {
                      onEvent(PaymentEvent.NavigateToEditItem(
                          saveItems = saveItems
                      ))
                  },
              items = state.items
          )
          TotalSections(
              modifier = Modifier.fillMaxWidth(),
              subTotal = state.total,
              grandTotal = state.total,
              totalItem = "${state.items.size}"
          )
      }
       SelectDate(
           modifier = Modifier,
           showDatePickerDialog = state.showDateDialog,
           onDismiss = {
               onEvent(PaymentEvent.ShowDateDialog(false))
           },
       ){
           val selectedDate =  it.selectedDateMillis?.toLocalDateWithMonthName()
           onEvent(PaymentEvent.SelectedDate(selectedDate ?: state.selectedDate))
       }


   }


}
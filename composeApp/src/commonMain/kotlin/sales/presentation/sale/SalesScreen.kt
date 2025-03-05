package sales.presentation.sale

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import core.util.UiEvent
import kotlinx.coroutines.flow.Flow
import sales.domain.enums.SalesTabs
import sales.domain.extensions.removeUnnecessaryDecimals
import sales.domain.model.ItemDetail
import sales.presentation.sale.component.ActionIcons
import sales.presentation.sale.component.EnterPrice
import sales.presentation.sale.component.ItemsSheet
import sales.presentation.sale.component.SalePages
import sales.presentation.sale.component.TextNumber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SalesScreen(
    state: SaleState,
    onEvent: (SaleEvent) -> Unit,
    uiEvent: Flow<UiEvent>,
    navController: NavController,
    snackBarHost: SnackbarHostState,
    saveItems: (List<ItemDetail>) -> Unit,
    items:List<ItemDetail>
){
    val pagerState = rememberPagerState(pageCount = { SalesTabs.entries.size})
    val selectedTabIndex = remember {
        derivedStateOf { pagerState.currentPage }
    }.value
    val scope = rememberCoroutineScope()



    LaunchedEffect(Unit){
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

   Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(8.dp)
   ) {
       TextNumber(
           modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp)
               .clickable {
                   if(state.items.isNotEmpty())
                       onEvent(SaleEvent.ShowSheet(true))

               },
           value = if(state.items.isEmpty()) "" else state.items.joinToString(separator = " | ")
       )
      ActionIcons(
          item = state.item,
          itemCount = state.items.size + 1
      )
      SalePages(
          pagerState = pagerState,
          selectedTabIndex = selectedTabIndex,
          scope = scope,
          onEvent = onEvent,
          enableCancelBtn = state.item.isNotEmpty() || state.items.isNotEmpty(),
          totalCash = state.totalCash,
          enableOperator = state.enableOperator,
          enableZBtn = state.enableZBtn,
          enableAtBtn = state.enableAtBtn,
          enableDotBtn = state.enableDotBtn,
          saveItems = saveItems
      )
   }

    EnterPrice(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f),
        showDialog = state.showEnterPriceDialog,
        onEvent = onEvent,
        zType = state.zType,
        changePrice = state.changePrice,
        enableDotBtn = state.enableDotBtnEnterPrice,
        enableEnterBtn = state.enableEnterBtnEnterPrice,
        previousPrice = state.tempPrice?.price?.removeUnnecessaryDecimals()
    )

    ItemsSheet(
      modifier = Modifier.fillMaxWidth(),
        items = state.products,
        showSheet = state.showSheet,
        onEvent = onEvent
    )
}


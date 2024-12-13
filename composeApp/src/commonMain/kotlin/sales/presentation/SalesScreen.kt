package sales.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sales.domain.enums.SalesTabs
import sales.presentation.component.ActionIcons
import sales.presentation.component.EnterPrice
import sales.presentation.component.SalePages
import sales.presentation.component.TextNumber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SalesScreen(
    state: SaleState,
    onEvent: (SaleEvent) -> Unit
){
    val pagerState = rememberPagerState(pageCount = { SalesTabs.entries.size})
    val selectedTabIndex = remember {
        derivedStateOf { pagerState.currentPage }
    }.value
    val scope = rememberCoroutineScope()

   Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(8.dp)
   ) {
       TextNumber(
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
          enableCancelBtn = state.item.isNotEmpty(),
          totalCash ="${state.totalCash}",
          enableOperator = state.enableOperator,
          enableZBtn = state.enableZBtn,
          enableAtBtn = state.enableAtBtn,
          enableDotBtn = state.enableDotBtn
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
        enableEnterBtn = state.enableEnterBtnEnterPrice
    )
}


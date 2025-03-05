package sales.presentation.sale.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import sales.domain.enums.SalesTabs
import sales.domain.model.ItemDetail
import sales.presentation.sale.SaleEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SalePages(
    pagerState: PagerState,
    selectedTabIndex: Int,
    scope: CoroutineScope,
    enableOperator:Boolean,
    enableCancelBtn:Boolean,
    totalCash:String,
    enableZBtn: Boolean,
    enableAtBtn: Boolean,
    enableDotBtn: Boolean,
    onEvent: (SaleEvent) -> Unit,
    saveItems:(List<ItemDetail>)->Unit
){

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        divider = {
            HorizontalDivider(
                thickness = 0.dp,
                color = MaterialTheme.colorScheme.surfaceContainer
            )
        },
        indicator = {}
    ){
        SalesTabs.entries.forEachIndexed { index, salesTab ->
            Tab(
                modifier = Modifier
                    .background(if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer),
                selected = selectedTabIndex == index,
                selectedContentColor = if(selectedTabIndex == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(salesTab.ordinal)
                    }
                }
            ){
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(salesTab.label).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = if(selectedTabIndex == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ){
            when(it){
                0 -> {
                    CalculatorPage(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        onEvent = onEvent,
                        enableCancelBtn = enableCancelBtn,
                        enableOperator = enableOperator,
                        totalCash = totalCash,
                        enableZBtn = enableZBtn,
                        enableAtBtn = enableAtBtn,
                        enableDotBtn = enableDotBtn,
                        saveItems = saveItems
                    )
                }
                1->{
                    Text(text = "sales")
                }
                2->{
                    Text(text = "saved")
                }
            }

        }
    }
}
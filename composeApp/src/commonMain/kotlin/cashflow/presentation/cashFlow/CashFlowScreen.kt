package cashflow.presentation.cashFlow

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cashflow.domain.enums.CashFlowTabs
import cashflow.domain.mapper.toDateMonthYearFormat
import cashflow.presentation.cashFlow.component.DateRange
import cashflow.presentation.cashFlow.component.IncomeExpensePage
import core.util.DATE_RANGE
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import settings.domain.enums.InventoryTabs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CashFlowScreen(
    state: CashFlowState,
    onEvent: (CashFlowEvent) -> Unit,
    navController: NavController
){
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { InventoryTabs.entries.size})
    val selectedTabIndex = remember {
        derivedStateOf { pagerState.currentPage }
    }
    val localDensity = LocalDensity.current
    var tabHeight by remember {
        mutableStateOf(0.dp)
    }




    LaunchedEffect(Unit){
        val dateRange = navController.currentBackStackEntry?.savedStateHandle?.get<String>(DATE_RANGE)
        dateRange?.let {range ->
            val (start,end) = range.split(",").map { LocalDate.parse(it) }
            onEvent(CashFlowEvent.SelectedDateRange(start..end))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.TopStart
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DateRange(
                modifier = Modifier
                    .fillMaxWidth(),
                dateRange = state.dateRange?.toDateMonthYearFormat() ?: "",
                onEvent = onEvent,
                navController = navController,
                showNextArrow = state.showNextArrow

            )

            TabRow(
                selectedTabIndex = selectedTabIndex.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .onGloballyPositioned {coordinate ->
                        tabHeight = with(localDensity){coordinate.size.height.toDp()}
                    }


                ,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                divider = {
                    HorizontalDivider(
                        thickness = 0.dp,
                        color = MaterialTheme.colorScheme.surfaceContainer
                    )
                },
                indicator = {}
            ) {
                CashFlowTabs.entries.forEachIndexed { index, cashFlowTabs ->
                    Tab(
                        modifier = Modifier
                            .height(tabHeight)
                            .background(if (selectedTabIndex.value == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer),
                        selected = selectedTabIndex.value == index,
                        selectedContentColor = if(selectedTabIndex.value == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(cashFlowTabs.ordinal)
//                                onEvent(InventoryEvent.CurrentPage(pagerState.currentPage))
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center

                        ) {
                            Text(
                                text = stringResource(cashFlowTabs.label).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = if(selectedTabIndex.value == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
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
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    contentAlignment = Alignment.Center
                ){
                    when(it){
                        0 -> {
                            IncomeExpensePage(
                                categories = state.categories,
                                onEvent = onEvent,
                                selectedCategory = state.selectedCategory,
                                showIncomeForm = state.showIncomeForm,
                                showCategoryDropDown = state.showCategoryDropDown
                            )

                        }
                        1->{ Text(text = "Purchase") }
                    }

                }
            }



        }

    }
}
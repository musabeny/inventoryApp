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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import cashflow.data.mapper.toDateMonthYearFormat
import cashflow.domain.enums.UserFilterType
import cashflow.presentation.cashFlow.component.DateRange
import cashflow.presentation.cashFlow.component.DeleteDialog
import cashflow.presentation.cashFlow.component.FilterSheet
import cashflow.presentation.cashFlow.component.IncomeExpensePage
import cashflow.presentation.cashFlow.component.PurchasePage
import core.component.AddCategory
import core.component.CustomTextField
import core.util.DATE_RANGE
import core.util.UiEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.search
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import settings.domain.enums.InventoryTabs
import settings.presentation.inventory.InventoryEvent
import settings.presentation.inventory.InventoryState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CashFlowScreen(
    state: CashFlowState,
    onEvent: (CashFlowEvent) -> Unit,
    navController: NavController,
    inventoryEvent: (InventoryEvent) -> Unit,
    inventoryState:InventoryState,
    snackBarHost: SnackbarHostState,
    uiEvent: Flow<UiEvent>,
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




    LaunchedEffect(true){
        val dateRange = navController.currentBackStackEntry?.savedStateHandle?.get<String>(DATE_RANGE)
        dateRange?.let {range ->
            val (start,end) = range.split(",").map { LocalDate.parse(it) }
            onEvent(CashFlowEvent.SelectedDateRange(start..end))
        }

        uiEvent.collect{event ->
            when(event){
                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
                is UiEvent.PopBackStack -> {
                    navController.navigateUp()
                }
                is UiEvent.ShowSnackBar -> {
                    snackBarHost.showSnackbar(event.message.asString())
                }
            }
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

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 2.dp
            ) {
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    leadingIcon = Icons.Filled.Search,
                    hint = Res.string.search,
                    value = state.searchText,
                    onValue = {
                        onEvent(CashFlowEvent.Search(it))
                    },
                    fontWeight = FontWeight.Normal,
                    hintStyle = MaterialTheme.typography.bodyMedium,
                    showLeadingIcon = true,
                    showTrailingIcon = true,
                    clearText = {
                      onEvent(CashFlowEvent.ClearSearchText)
                    }
                )
            }


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
                                onEvent(CashFlowEvent.SelectedPageIndex(cashFlowTabs.ordinal))
                                pagerState.animateScrollToPage(cashFlowTabs.ordinal)
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
                                showIncomeForm = state.showIncomeOrExpenseForm,
                                showCategoryDropDown = state.showCategoryDropDown,
                                amount = state.amount ,
                                note = state.note,
                                today = state.today,
                                incomeExpenseType = state.incomeExpenseType,
                                viewType = state.viewType,
                                groupedByDate = state.incomeExpenses,
                                totalIncome = state.totalIncome,
                                totalExpense = state.totalExpense,
                                navController = navController,
                                incomeExpensesGroup = state.incomeExpensesGroup
                            ){
                                inventoryEvent(InventoryEvent.ShowBottomSheet(true))
                                onEvent(CashFlowEvent.ShowCategoryDropDown(false))
                                onEvent(CashFlowEvent.ShowIncomeExpenseForm(false,state.incomeExpenseType))
                            }

                        }
                        1->{
                            PurchasePage(
                                onEvent = onEvent,
                                navController = navController,
                                purchasedByDate = state.purchaseByDate,
                                viewType = state.viewType,
                                totalPurchase = state.purchaseTotal,
                                purchasedByItem = state.purchaseGroupedByItem
                            )
                        }
                    }

                }
            }



        }
        AddCategory(
            showBottomSheet = inventoryState.showBottomSheet,
            onEvent = inventoryEvent,
            colors = inventoryState.productColors,
            selectedProductColor = inventoryState.selectedProductColor,
            categoryName = inventoryState.categoryName,
            categoryError = inventoryState.categoryError,
            category = inventoryState.selectedCategory
        )
        FilterSheet(
            modifier = Modifier.fillMaxWidth(),
            showSheet = state.showFilterSheet,
            onEvent = onEvent,
            filters = when(state.userFilterType){
               UserFilterType.ENTRY -> state.entryType
                UserFilterType.MEMBERS -> state.members
                UserFilterType.INCOME -> state.incomeCategory
                UserFilterType.EXPENSE -> state.expenseCategory
                UserFilterType.STATUS -> state.status
            },
            selectedFilterType = state.userFilterType,
            tabs = state.selectedTabIndex
        )

        DeleteDialog(
            modifier = Modifier,
            showDialog = state.showDeleteDialog,
            onEvent = onEvent,
            selectedIncomeExpense = state.selectedIncomeExpense,
            type = state.viewType
        )

    }
}
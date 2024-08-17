package settings.presentation.inventory

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.navigation.NavHostController
import core.util.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import settings.domain.enums.InventoryTabs
import settings.presentation.inventory.component.CategoriesTab
import settings.presentation.inventory.component.InventoryTab

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InventoryScreen(
    state: InventoryState,
    onEvent:(InventoryEvent) -> Unit,
    navController: NavHostController,
    uiEvent: Flow<UiEvent>,
    snackBarHost: SnackbarHostState,
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
        uiEvent.collect{event ->
            when(event){
                is UiEvent.ShowSnackBar ->{
                    snackBarHost.showSnackbar(event.message)
                }
                is UiEvent.Navigate ->{
                    navController.popBackStack()
                }
                else ->{}
            }

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {

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
            InventoryTabs.entries.forEachIndexed { index, inventoryTabs ->
                Tab(
                    modifier = Modifier
                        .height(tabHeight)
                        .background(if (selectedTabIndex.value == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer),
                    selected = selectedTabIndex.value == index,
                    selectedContentColor = if(selectedTabIndex.value == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(inventoryTabs.ordinal)
                            onEvent(InventoryEvent.CurrentPage(pagerState.currentPage))
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = stringResource(inventoryTabs.label).uppercase(),
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
                        InventoryTab(
                            modifier = Modifier.fillMaxSize(),
                            onEvent = onEvent,
                            navController = navController,
                            products = state.products,
                            showOnlyExpired = state.showOnlyExpired
                        )
                    }
                    1->{
                        CategoriesTab(
                            modifier = Modifier
                                .fillMaxSize(),
                            onEvent = onEvent,
                            showBottomSheet = state.showBottomSheet,
                            colors = state.productColors,
                            selectedProductColor = state.selectedProductColor,
                            categoryName = state.categoryName,
                            categoryError = state.categoryError,
                            categories = state.categories,
                            category = state.selectedCategory

                        )
                    }
                    2->{ Text(text = "purchase") }
                }

            }
        }

    }

}
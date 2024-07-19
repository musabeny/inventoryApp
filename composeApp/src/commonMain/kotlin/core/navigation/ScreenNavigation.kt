package core.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import core.component.CustomTextField
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.calculate
import inventoryapp.composeapp.generated.resources.compose_multiplatform
import inventoryapp.composeapp.generated.resources.product
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import settings.presentation.inventory.InventoryEvent
import settings.presentation.inventory.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun ScreenNavigation(){
    val navigation = rememberNavController()

    val screens = listOf(
        Routes.Report,
        Routes.Sales,
        Routes.CashFlow,
        Routes.Settings
    )
    val topAppBarScreen = listOf(
        Routes.Report,
        Routes.Sales,
        Routes.CashFlow,
        Routes.Settings,
        Routes.Inventory,
        Routes.Product
    )
    val showNavigationIcon = listOf(
        Routes.Inventory,
        Routes.Product
    )
    val showTitleNavigation = listOf(
        Routes.Product
    )
    val navBackStackEntry by navigation.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination?.route

    val snackBarHost = remember{ SnackbarHostState() }

    val inventoryViewModel = koinViewModel<InventoryViewModel>()
    val inventoryState = inventoryViewModel.state.collectAsState().value
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if(currentDestination in topAppBarScreen.map { it.route } ){
               if(currentDestination in showTitleNavigation.map { it.route }){
                   CenterAlignedTopAppBar(
                       title = {
                           Text(
                               text = stringResource(topAppBarScreen.find { it.route == currentDestination }?.title ?: Res.string.product),
                               color = MaterialTheme.colorScheme.primary,
                               style = MaterialTheme.typography.bodyMedium,
                               fontWeight = FontWeight.SemiBold
                               )
                       },
                       modifier = Modifier.fillMaxWidth(),
                       colors = TopAppBarDefaults.topAppBarColors().copy(
                           containerColor = if(currentDestination in showNavigationIcon.map { it.route }) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                       ),
                       navigationIcon = {
                           NavigationIcon(navigation)
                       }
                   )
               }else{
                   TopAppBar(
                       title = {
                           if(currentDestination !in showNavigationIcon.map { it.route }){
                               Icon(
                                   painter = painterResource(Res.drawable.calculate),
                                   contentDescription = null,
                                   modifier = Modifier.size(48.dp),
                                   tint = MaterialTheme.colorScheme.onPrimary
                               )
                           }else if(currentDestination ==  Routes.Inventory.route){
                               Box(
                                  modifier = Modifier
                                      .fillMaxSize(),
                                   contentAlignment = Alignment.CenterEnd
                               ){
                                   if(inventoryState.showSearchIcon){
                                       Icon(
                                           modifier = Modifier.clickable {
                                               inventoryViewModel.onEvent(InventoryEvent.ShowSearchIcon)
                                           },
                                           imageVector = Icons.Filled.Search,
                                           contentDescription = "Search icons",
                                           tint = MaterialTheme.colorScheme.primary
                                       )
                                   }else{
                                       CustomTextField(
                                           modifier = Modifier
                                               .fillMaxWidth(),
                                           errorMessage = null,
                                           showTrailingIcon = true,
                                          clearText = {
                                              inventoryViewModel.onEvent(InventoryEvent.ClearSearchText)
                                          } ,
                                           onValue = {
                                               inventoryViewModel.onEvent(InventoryEvent.EnterSearchText(it))
                                           },
                                           value = inventoryState.searchText
                                       )
                                   }
                               }



                           }

                       },
                       actions = {},
                       modifier = Modifier.fillMaxWidth(),
                       colors = TopAppBarDefaults.topAppBarColors().copy(
                           containerColor = if(currentDestination in showNavigationIcon.map { it.route }) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                       ),
                       navigationIcon = {
                           if(currentDestination in showNavigationIcon.map { it.route }){
                               NavigationIcon(navigation)
                           }
                       }
                   )
               }


            }


        },
        bottomBar = {
            if(currentDestination in screens.map { it.route } ){
                BottomBar(
                    destination = navigation.currentDestination,
                    navController = navigation,
                    screens = screens,
                    selected = navigation.currentDestination?.route
                )
            }


        },
        snackbarHost = { SnackbarHost(hostState = snackBarHost) }
    ){
        Box(
            modifier = Modifier.padding(top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding())
        ) {
            ScreenNavHost(
                navHostController = navigation,
                snackBarHost = snackBarHost,
                inventoryViewModel = inventoryViewModel,
                inventoryState = inventoryState
            )
        }

    }

}

@Composable
fun BottomBar(
    destination: NavDestination?,
    navController: NavHostController,
    screens: List<Routes>,
    selected:String?
){

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary
    ) {
        var selectedScreen by remember { mutableIntStateOf(1) }
        screens.forEachIndexed {index,screen ->
            NavigationBarItem(
                modifier = Modifier.background(
                    if(selectedScreen == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                ),
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    selectedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.primary,
                ),
               selected = selectedScreen == index ,
                onClick = {
                    selectedScreen = index
                   navController.navigate(screen.route){
                       popUpTo(navController.graph.findStartDestination().navigatorName)
                       launchSingleTop = true
                   }
                },
                icon ={

                      Icon(
                          painter = painterResource(screen.defaultIcon ?: Res.drawable.compose_multiplatform),
                          contentDescription = "",
                          modifier = Modifier.size(40.dp)
                      )
                },
                label = {
                    Text(
                        text = stringResource(screen.title),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}

@Composable
fun NavigationIcon(
    navigation: NavHostController
){
    IconButton(
        onClick = {
            navigation.navigateUp()
        }
    ){
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}


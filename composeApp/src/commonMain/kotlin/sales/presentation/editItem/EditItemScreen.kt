package sales.presentation.editItem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import core.util.ITEMS
import core.util.UiEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_new_item
import inventoryapp.composeapp.generated.resources.save
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import sales.domain.model.ItemDetail
import sales.presentation.components.ActionsButton
import sales.presentation.editItem.component.EditItemTopBar
import sales.presentation.editItem.component.Item

@Composable
fun EditItemScreen(
    items:List<ItemDetail>,
    state: EditItemState,
    onEvent: (EditItemEvent) -> Unit,
    navController: NavController,
    uiEvent: Flow<UiEvent>,
    saveItem:(List<ItemDetail>) -> Unit
){

    LaunchedEffect(key1 = true){
        onEvent(EditItemEvent.GetItems(items))
        uiEvent.collect{event ->
            when(event){
                is  UiEvent.Navigate ->{
                    navController.navigate(event.route)
                }
                is UiEvent.PopBackStack -> {
                    navController.navigateUp()
                }
                is UiEvent.ShowSnackBar -> {

                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
         EditItemTopBar{
           onEvent(EditItemEvent.NavigateBack)
         }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
                .padding(bottom = 8.dp)
                .background(color = MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.BottomCenter
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(state.items){index,item ->

                   Item(
                       itemName = item.product ?: "",
                       price = "${item.itemPrice ?: 0.0 }",
                       quantity = if(item.quantity == 0) "" else "${item.quantity ?: 0}",
                       index = index,
                       onEvent = onEvent
                   )
                }
            }

            ActionsButton(
                labelOne = Res.string.add_new_item,
                labelTwo = Res.string.save,
                btnOne = {},
                btnTwo = {
                    onEvent(EditItemEvent.Save(saveItems = saveItem))
                }
            )
        }

    }
}
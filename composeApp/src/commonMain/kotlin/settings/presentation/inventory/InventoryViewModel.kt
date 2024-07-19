package settings.presentation.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.navigation.Routes
import core.util.PRODUCT_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import settings.domain.repository.ProductRepository

class InventoryViewModel(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(InventoryState())
    val state = _state.asStateFlow()

    init {
        onEvent(InventoryEvent.GetProduct)
    }
    fun onEvent(event: InventoryEvent){
        when(event){
            is InventoryEvent.GoToAddItem ->{
                event.navController.navigate(Routes.Product.route.replace("{$PRODUCT_ID}","0"))
            }
            is InventoryEvent.GetProduct ->{
               repository.products().onEach {products ->
                   _state.update {
                       it.copy(products = products)
                   }
               }.launchIn(viewModelScope)
            }
            is InventoryEvent.SelectProduct ->{
                event.navController.navigate(Routes.Product.route.replace("{$PRODUCT_ID}","${event.productId}"))
            }
            is InventoryEvent.ShowSearchIcon ->{
                _state.update {
                    it.copy(showSearchIcon = !_state.value.showSearchIcon)
                }
            }
            is InventoryEvent.ClearSearchText ->{
                _state.update {
                    it.copy(searchText = "")
                }
                onEvent(InventoryEvent.ShowSearchIcon)
                onEvent(InventoryEvent.GetProduct)
            }
            is InventoryEvent.EnterSearchText ->{
                _state.update {
                    it.copy(searchText = event.searchText)
                }
                if(_state.value.searchText.isBlank()){
                    onEvent(InventoryEvent.GetProduct)
                }else{
                    repository.searchProduct("*${_state.value.searchText}*").onEach {products ->
                        _state.update {
                            it.copy(products = products)
                        }
                    }.launchIn(viewModelScope)
                }


            }
            is InventoryEvent.ShowOnlyExpired ->{
                _state.update {
                    it.copy(showOnlyExpired = !_state.value.showOnlyExpired)
                }
                if(_state.value.showOnlyExpired){

                }
            }
        }
    }
}
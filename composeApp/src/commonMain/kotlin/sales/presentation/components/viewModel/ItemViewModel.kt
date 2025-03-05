package sales.presentation.components.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import sales.domain.model.ItemDetail

class ItemViewModel: ViewModel() {
   private val _items = MutableStateFlow(emptyList<ItemDetail>())
    val item = _items.asStateFlow()

    fun saveItems(itemDetail: List<ItemDetail>){
        _items.value = itemDetail
        println("itemDetails vm ${_items.value}  ")
    }
}
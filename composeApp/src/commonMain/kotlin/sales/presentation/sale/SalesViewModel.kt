package sales.presentation.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.navigation.Routes
import core.util.ITEMS
import core.util.UiEvent
import core.util.UiText
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_atleast_item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import sales.domain.enums.ClearAction
import sales.domain.extensions.powerOf
import sales.domain.extensions.removeUnnecessaryDecimals
import sales.domain.model.ItemDetail
import sales.domain.model.TemporaryPrice
import sales.domain.repository.SalesRepository
import sales.domain.useCase.SaleUseCases
import sales.domain.useCase.useCases.CalculateItems
import settings.domain.repository.ProductRepository

class SalesViewModel(
    private val useCases: SaleUseCases,
    private val productRepository: ProductRepository,
    private val calculateItems: CalculateItems,
    private val repository: SalesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SaleState())
    val state = _state.asStateFlow()

    private val itemList = mutableListOf("")
    private val changePrice:StringBuilder = StringBuilder()
    private val listOfOperator = listOf("+","-","x","÷")
    val items = mutableListOf<ItemDetail>()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init {
        itemList.clear()
    }

    fun onEvent(event: SaleEvent){
        when(event){
            is SaleEvent.Clear ->{

                when(event.clear){
                    ClearAction.SingleCharacter -> {
                        if(event.isChangePrice){
                            if(changePrice.isEmpty()){
                                return
                            }
                            changePrice.deleteAt(changePrice.lastIndex)
                            _state.update {
                                it.copy(changePrice = changePrice.toString())
                            }
                            _state.update {
                                it.copy(enableDotBtnEnterPrice = !useCases.dotBtn(changePrice,true))
                            }
                            _state.update {
                                it.copy(enableEnterBtnEnterPrice = _state.value.changePrice.isNotEmpty())
                            }
                        }else{
                            if(_state.value.tapValue.isEmpty()){
                                itemList.clear()
                                itemList.addAll(_state.value.items)
                                if(itemList.isNotEmpty())
                                itemList.removeLast()
                                _state.update {
                                    it.copy(items = itemList.toList())
                                }
                            }else{
                                _state.value.tapValue.deleteAt(_state.value.tapValue.lastIndex)
                            }
                        }

                    }
                    ClearAction.AllCharacter -> {
                        _state.value.tapValue.clear()
                        _state.update {
                            it.copy(item = _state.value.tapValue.toString())
                        }
                        if(_state.value.item.isEmpty() && _state.value.items.isNotEmpty()){
                            _state.update {
                                it.copy(
                                    items = emptyList()
                                )
                            }
                        }
                        itemList.clear()
                        itemList.addAll(_state.value.items)
                    }
                }
                if(!event.isChangePrice){
                    mathExpression("")
                }
                updateUiStatus()



            }
            is SaleEvent.Operand -> {
                if(event.isChangePrice){
                    changePriceState(event.operand)
                }else{
                    mathExpression(event.operand)
                }


            }
            is SaleEvent.Operator -> {
                mathExpression(event.operator)


            }
            is SaleEvent.SpecialCharacter ->{
                if(event.isChangePrice){
                    changePriceState(event.value)
                }else{
                    mathExpression(if(event.value == "@")"" else event.value)
                    if(event.value == "@"){
                        onEvent(SaleEvent.ShowEnterPriceDialog(true))
                    }
                }

            }
            is SaleEvent.AddItem -> canShowEnterPriceDialog(canAdd = true)
            is SaleEvent.ShowEnterPriceDialog ->{
                if(_state.value.showEnterPriceDialog){
                    val  isOperator=   _state.value.tapValue.lastOrNull().toString()
                    if(listOfOperator.any { it == isOperator }){
                        _state.value.tapValue.deleteAt(_state.value.tapValue.lastIndex)
                        updateUiStatus()
                    }
                }
                _state.update {
                    it.copy(showEnterPriceDialog = event.show)
                }

                if(!event.show){
                    _state.update {
                        it.copy(changePrice = "")
                    }
                }

            }
            is SaleEvent.ShowSheet ->{
                _state.update {
                    it.copy(showSheet = event.show)
                }
            }
            is SaleEvent.Navigate ->{
                if(_state.value.products.isNotEmpty()){
//                    val items =  Json.encodeToString(_state.value.products)
                    event.saveItems(_state.value.products)
                    sendEvent(UiEvent.Navigate(Routes.Payments.route))
                }else{
                    sendEvent(UiEvent.ShowSnackBar(message = UiText.StringResources(Res.string.add_atleast_item)))
                }

            }
            is SaleEvent.UpdatedItems ->{
                _state.update {
                    it.copy(products = event.items)
                }
            }
            is SaleEvent.SaveTemporaryPrice ->{
                viewModelScope.launch {
                    val price = _state.value.changePrice.toDoubleOrNull()
                    val code = _state.value.zType
                    if(price != null && code != null){
                        val tempPrice = TemporaryPrice(code = code, price = price)
                         repository.saveTemporaryPrice(tempPrice)
                        _state.value.zType?.let{codePrice ->
                            val getTempPrice =   repository.getTemporaryPrice(codePrice)
                            _state.update {
                                it.copy(tempPrice = getTempPrice)
                            }
                        }
                        addItem()
                    }
                    onEvent(SaleEvent.ShowEnterPriceDialog(false))


                }
            }
        }
    }

    private fun changePriceState(value: String){
        changePrice.append(value)
        _state.update {
            it.copy(changePrice = changePrice.toString())
        }
        _state.update {
            it.copy(enableDotBtnEnterPrice = !useCases.dotBtn(changePrice,true))
        }
        _state.update {
            it.copy(enableEnterBtnEnterPrice = _state.value.changePrice.isNotEmpty())
        }

    }
    private fun mathExpression(value:String){

         val isOperator = listOfOperator.any { it == value }
         if(isOperator && _state.value.enableAtBtn){
             canShowEnterPriceDialog()
         }
            if(value.isNotEmpty()){
                if(value == "Z" && _state.value.tapValue.lastOrNull()?.isDigit() == true){
                    _state.value.tapValue.append("x$value")
                }else{
                    _state.value.tapValue.append(value)
                }
                updateUiStatus()
            }


    }
    private fun updateUiStatus(){
        _state.update {
            it.copy(item = _state.value.tapValue.toString())
        }
        _state.update {
            it.copy(enableOperator = _state.value.tapValue.lastOrNull()?.isDigit() ?: false )
        }
        _state.update {
            it.copy(enableZBtn = useCases.zBtn(_state.value.tapValue))
        }
        _state.update {
            it.copy(enableDotBtn = useCases.dotBtn(_state.value.tapValue))
        }
        val atBtn = useCases.atBtn(state.value.tapValue)
        _state.update {
            it.copy(enableAtBtn = atBtn.first)
        }
        _state.update {
            it.copy(zType = atBtn.second)
        }
        if(_state.value.items.isEmpty()){
            items.clear()
            _state.update {
                it.copy(products =items)
            }
        }

    }
    private fun canShowEnterPriceDialog(canAdd:Boolean = false){
     viewModelScope.launch {
         changePrice.clear()
         _state.update {
             it.copy(tempPrice = null)
         }
         val productId =  useCases.extractNumber(_state.value.zType ?: "")
         _state.value.zType?.let {code ->
             val getTempPrice =   repository.getTemporaryPrice(code)
             _state.update {
                 it.copy(tempPrice = getTempPrice)
             }
             changePriceState(_state.value.tempPrice?.price?.removeUnnecessaryDecimals() ?: "")


         }


         if(productId != null){
             val product =   productRepository.getProductById(productId)
             if(product == null){
                 _state.update {
                     it.copy(showEnterPriceDialog = true)

                 }

             }else{
                 _state.update {
                     it.copy(product = product)
                 }
                 if(canAdd){
                     addItem()
                 }
             }
         }else{
             if(canAdd){
                 addItem()
             }
         }


     }
    }
    private fun addItem(){
        viewModelScope.launch {
            if(_state.value.item.isEmpty()){
                return@launch
            }
            val result = useCases.expression(_state.value.item,itemList)

            result.first?.let {
                if(result.second != null){
                    println("addItem !! ${result.second} ${_state.value.item} ")
                    itemList[result.second!!] = it
                }else{
                    println("addItem ${_state.value.tempPrice}")
                   val addItem = if(_state.value.tempPrice == null)  it else "$it=${_state.value.tempPrice?.price ?: 0.0}"
                    itemList.add(addItem)
                }
            }

            _state.update {
                it.copy(items = itemList.toList())
            }
           val item = calculateItems(_state.value.items,_state.value.item,items.size)
           item?.let {
               items.add(it)
           }
            _state.update {
                it.copy(products = items)
            }

            val sumProduct = _state.value.products.sumOf {
                if (it.itemPrice == null && it.total == null) {
                 0.0
             } else {
                  (it.total ?: 0.0)
             }
         }
            _state.update {
                    it.copy(totalCash = sumProduct.removeUnnecessaryDecimals() )
                }

            _state.value.tapValue.clear()
            _state.update {
                it.copy(item = "")
            }
            updateUiStatus()
        }
    }
    private  fun sendEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }

}
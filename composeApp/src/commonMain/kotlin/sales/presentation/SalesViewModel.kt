package sales.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.util.isAllDigits
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sales.domain.enums.ClearAction
import sales.domain.extensions.removeUnnecessaryDecimals
import sales.domain.useCase.SaleUseCases
import sales.domain.useCase.useCases.CalculateItems
import settings.domain.repository.ProductRepository

class SalesViewModel(
    private val useCases: SaleUseCases,
    private val productRepository: ProductRepository,
    private val calculateItems: CalculateItems
) : ViewModel() {
    private val _state = MutableStateFlow(SaleState())
    val state = _state.asStateFlow()

    private val itemList = mutableListOf("")
    private val changePrice:StringBuilder = StringBuilder()
    private val listOfOperator = listOf("+","-","x","÷")

    init {
        itemList.clear()
    }

    fun onEvent(event: SaleEvent){
        when(event){
            is SaleEvent.Clear ->{

                when(event.clear){
                    ClearAction.SingleCharacter -> {
                        if(event.isChangePrice){
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
                    }
                }
                if(!event.isChangePrice){
                    mathExpression("")
                }


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
            is SaleEvent.AddItem -> addItem()
            is SaleEvent.ShowEnterPriceDialog ->{
                if(_state.value.showEnterPriceDialog){
                    val  isOperator=   _state.value.tapValue.last().toString()
                    if(listOfOperator.any { it == isOperator }){
                        _state.value.tapValue.deleteAt(_state.value.tapValue.lastIndex)
                        updateUiStatus()
                    }
                }
                _state.update {
                    it.copy(showEnterPriceDialog = event.show)
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

    }
    private fun canShowEnterPriceDialog(){
     viewModelScope.launch {
         _state.value.zType?.let {ztype ->
             val productId =  useCases.extractNumber(ztype)
             productId?.let {id ->
                 val product =   productRepository.getProductById(id)
                 if(product == null){
                     _state.update {
                         it.copy(showEnterPriceDialog = true)
                     }
                 }else{
                     _state.update {
                         it.copy(product = product)
                     }
                 }
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
                    itemList[result.second!!] = it
                }else{
                    itemList.add(it)
                }

            }

            _state.update {
                it.copy(items = itemList.toList())
            }
           val products = calculateItems(_state.value.items)
            _state.update {
                it.copy(products = products)
            }

         val sumProduct = _state.value.products.sumOf {
             if (it.product?.price == null && it.count == null) {
                 0.0
             } else {
                 (it.product?.price ?: 1.0) * (it.count ?: 1.0)
             }
         }
            _state.update {
                    it.copy(totalCash = sumProduct.removeUnnecessaryDecimals() )
                }

            onEvent(SaleEvent.Clear(ClearAction.AllCharacter))
            updateUiStatus()
        }
    }

}
package settings.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.util.UiEvent
import core.util.UiText
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.itemName
import inventoryapp.composeapp.generated.resources.item_code_exist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import settings.data.mapper.toLocalDate
import settings.domain.enums.ProductForm
import settings.domain.model.product.Product
import settings.domain.repository.ProductRepository
import settings.domain.useCase.ProductUseCase

class ProductViewModel(
    private val repository: ProductRepository,
    private val useCase: ProductUseCase
):ViewModel() {
 private val _state = MutableStateFlow(ProductState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        onEvent(ProductEvent.GetProductColors)
        onEvent(ProductEvent.SelectColor(useCase.color()[0]))
        onEvent(ProductEvent.LastItemCode)
    }

    fun onEvent(event: ProductEvent){
        when(event){
            is ProductEvent.AddProduct -> addProduct()
            is ProductEvent.EnterBarcode -> {
                _state.update {
                    it.copy(barcode = event.barcode)
                }
            }
            is ProductEvent.EnterExpireDateAlert -> {
                _state.update {
                    it.copy(expireDateAlert = event.days)
                }
            }
            is ProductEvent.EnterItemCode -> {
                _state.update {
                    it.copy(code = event.code)
                }
               val result =  useCase.validate(ProductForm.Code,event.code)
                if(result == null){
                    onEvent(ProductEvent.FindItemCode(_state.value.code,true))
                    _state.update {
                        it.copy(codeError = null)
                    }
                }else{
                    _state.update {
                        it.copy(codeError = result)
                    }
                }

            }
            is ProductEvent.EnterItemName -> {
                _state.update {
                    it.copy(name = event.name)
                }
            }
            is ProductEvent.EnterItemPrice -> {
                _state.update {
                    it.copy(price = event.price)
                }
            }
            is ProductEvent.PriceCheckOption -> {
                _state.update {
                    it.copy(priceCheck = !_state.value.priceCheck)
                }


            }
            is ProductEvent.ScanBarcode -> {}
            is ProductEvent.SelectColor -> {
                _state.update {
                    it.copy(productColors = useCase.color() )
                }
                _state.update {
                    it.copy(color = event.color)
                }
                _state.update {
                    it.copy(productColors = _state.value.productColors.map { if(it.id == event.color.id) it.copy(isSelected = !event.color.isSelected) else it } )
                }
            }
            is ProductEvent.GetProductColors ->{
                _state.update {
                    it.copy(productColors = useCase.color())
                }
            }
            is ProductEvent.SelectExpireDate -> {}
            is ProductEvent.DatePickerDialog ->{
                _state.update {
                    it.copy(showDatePickerDialog = event.show)
                }
            }
            is ProductEvent.SelectedDate ->{
                _state.update {
                    it.copy(expireDate = event.dateSelected)
                }
                _state.value.expireDate?.let {date ->
                    val dateFormated = useCase.dateMillsToDate(date)
                    _state.update {
                        it.copy(expireDateFormatted ="${dateFormated.dayOfMonth}/${dateFormated.monthNumber}/${dateFormated.year}" )
                    }
                }

                onEvent(ProductEvent.DatePickerDialog(false))
            }
            is ProductEvent.IsPriceRequired ->{
                _state.update {
                    it.copy(priceNotRequired = !_state.value.priceNotRequired)
                }

                if(_state.value.priceNotRequired){
                    _state.update {
                        it.copy(priceError = null)
                    }

                    _state.update {
                        it.copy(price = null)
                    }
                }
            }
            is ProductEvent.FindItemCode -> findItemCode(event.itemCode,event.userSearch)
           is ProductEvent.LastItemCode -> lastItemCode()
            is ProductEvent.GetProductById -> getProductById(event.productId)
            else ->{}
        }
    }

    private fun addProduct(){
        viewModelScope.launch {
//            println("code is ${state.value.code}")
            _state.update {
                it.copy(codeError = useCase.validate(ProductForm.Code,state.value.code))
            }
            _state.update {
                it.copy(nameError = useCase.validate(ProductForm.Name,state.value.name))
            }
            if(!_state.value.priceNotRequired){
                _state.update {
                    it.copy(priceError = useCase.validate(ProductForm.Price,state.value.price ?: ""))
                }
            }

            val validation =  listOf(
                _state.value.priceError,
                _state.value.codeError,
                _state.value.nameError
            ).any {it != null}
            _state.update {
                it.copy(validationSuccess = !validation)
            }


            if(_state.value.validationSuccess){
                val product = Product(
                    id = _state.value.productId,
                    code = _state.value.code,
                    name = _state.value.name,
                    price = _state.value.price?.toDoubleOrNull(),
                    color = _state.value.color,
                    barcode = _state.value.barcode,
                    expireDate = _state.value.expireDate,
                    expireDateAlert = _state.value.expireDateAlert?.toIntOrNull(),
                    dateCreated = Clock.System.now().toEpochMilliseconds()
                )

                val result = repository.insertOrUpdateProduct(product)
               println("update product $result")
                if(result > 0){
//                    sendEvent(UiEvent.ShowSnackBar(UiText.DynamicText("Product saved Successfully").value))
                    sendEvent(UiEvent.PopBackStack)
                }else{
                    sendEvent(UiEvent.ShowSnackBar(UiText.DynamicText("Fail to save product")))
                }
            }else{
                sendEvent(UiEvent.ShowSnackBar(UiText.DynamicText("Please fill all the necessary field")))
            }

        }
    }

    private fun findItemCode(code:String,isUserSearch:Boolean){
        viewModelScope.launch {
            val itemCode = withContext(Dispatchers.IO){
                repository.findByItemCode(code)
            }
            if(isUserSearch){
                if(itemCode == null){
                    _state.update {
                        it.copy(itemCodeFoundError = null)
                    }
                }else{
                    _state.update {
                        it.copy(itemCodeFoundError = Res.string.item_code_exist )
                    }
                }

            }else{
                itemCode?.let {code ->
                    val newItemCode = (code.toLongOrNull() ?: 0) + 1
                    _state.update {
                        it.copy(code = "$newItemCode")
                    }
                }
            }

            println("Item code $itemCode")
        }
    }

    private fun lastItemCode(){
        viewModelScope.launch(Dispatchers.IO) {
            val itemCode = repository.getLastItemCode()
            if(itemCode == null){
                val productCount = repository.countProduct()
                if(productCount == 0){
                    _state.update {
                        it.copy(code = "1")
                    }
                }
            }else{
                onEvent(ProductEvent.FindItemCode(itemCode,false))
            }
            println("Item code $itemCode productCount ")
        }
    }

    private fun getProductById(productId:Long){
        if(productId<=0){
            _state.update {
                it.copy(productId = null)
            }
            return
        }
        viewModelScope.launch {
            val product = withContext(Dispatchers.IO){
                repository.getProductById(productId)
            }
            if(product != null){
                _state.update {
                    it.copy(productId = product.id)
                }
                _state.update {
                    it.copy(color = product.color)
                }
                _state.update {
                    it.copy(code = product.code)
                }
                _state.update {
                    it.copy(name = product.name)
                }
                _state.update {
                    it.copy(price = "${product.price}")
                }

                onEvent(ProductEvent.SelectColor( product.color))

                _state.update {
                    it.copy(expireDateFormatted = product.expireDate?.toLocalDate().toString())
                }
                _state.update {
                    it.copy(expireDate = product.expireDate)
                }

                _state.update {
                    it.copy(expireDateAlert = "${product.expireDateAlert}")
                }
            }else{
                _state.update {
                    it.copy(productId = null)
                }
            }
        }
    }


   private  fun sendEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }



}
package settings.presentation.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.navigation.Routes
import core.util.PRODUCT_ID
import core.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import settings.data.mapper.toCategory
import settings.data.mapper.toCategoryWithColor
import settings.domain.enums.InventorySearchType
import settings.domain.model.category.Category
import settings.domain.model.category.CategoryWithColor
import settings.domain.repository.CategoryRepository
import settings.domain.repository.ProductRepository
import settings.domain.useCase.CategoryUseCase
import settings.domain.useCase.ProductUseCase

class InventoryViewModel(
    private val repository: ProductRepository,
    private val useCase: ProductUseCase,
    private val categoryUseCase: CategoryUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InventoryState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        onEvent(InventoryEvent.GetProduct)
        onEvent(InventoryEvent.GetProductColor)
        onEvent((InventoryEvent.SelectColor(useCase.color()[0])))
        onEvent(InventoryEvent.GetAllCategories)
    }

    fun onEvent(event: InventoryEvent){
        when(event){
            is InventoryEvent.GoToAddItem ->{
                event.navController.navigate(Routes.Product.route.replace("{$PRODUCT_ID}","0"))
            }
            is InventoryEvent.GetProduct -> getAllProduct()
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
            is InventoryEvent.EnterSearchText ->searchByName(event.searchText)
            is InventoryEvent.ShowOnlyExpired -> showExpiredProduct()
            is InventoryEvent.ShowBottomSheet ->{
                _state.update {
                    it.copy(showBottomSheet = event.bottomSheet)
                }
                _state.update {
                    it.copy(selectedCategory = null)
                }
                println("Cashflow action executed ${_state.value.showBottomSheet}")
            }
            is InventoryEvent.SaveCategories ->saveOrUpdateCategory()
            is InventoryEvent.GetProductColor ->{
                _state.update {
                    it.copy(productColors = useCase.color())
                }
            }
            is InventoryEvent.SelectColor->{
                _state.update {
                    it.copy(selectedProductColor = event.productColor)
                }
            }
            is InventoryEvent.EnterCategoryName->{
                _state.update {
                    it.copy(categoryName = event.categoryName)
                }
            }
            is InventoryEvent.GetAllCategories ->categories()
            is InventoryEvent.SelectCategory ->selectCategory(event.category)
            is InventoryEvent.DeleteCategory ->deleteCategory(event.category)
            is InventoryEvent.CurrentPage ->selectedPage(event.index)
            else ->{}
        }
    }


    private fun getAllProduct(){
        repository.products().onEach {products ->
            _state.update {
                it.copy(products = products)
            }
        }.launchIn(viewModelScope)
    }

    private fun getExpiredProduct(){
        repository.getExpiredProducts().onEach {products ->
            _state.update {
                it.copy(products = products)
            }
        }.launchIn(viewModelScope)
    }

    private fun showExpiredProduct(){
        _state.update {
            it.copy(showOnlyExpired = !_state.value.showOnlyExpired)
        }
        if(_state.value.showOnlyExpired){
            getExpiredProduct()
        }else{
            getAllProduct()
        }
    }

    private fun saveOrUpdateCategory(){
        viewModelScope.launch {

            _state.update {
                it.copy(categoryError = categoryUseCase.validate(_state.value.categoryName))
            }
            if(_state.value.categoryError != null){
                return@launch
            }

            val category = Category(
                id = _state.value.selectedCategory?.id,
                name = _state.value.categoryName,
                colorId = _state.value.selectedProductColor?.id ?: 0
            )
            if(_state.value.selectedCategory != null){
               val updateResult = categoryRepository.updateCategory(category)
                if(updateResult > 0){
                    _state.update {
                        it.copy(showBottomSheet = false)
                    }
                    _state.update {
                        it.copy(categoryName = "")
                    }
                    sendEvent(UiEvent.ShowSnackBar(message = "Category updated Successfully"))
                }else{
                    sendEvent(UiEvent.ShowSnackBar(message = "Fail to update Category"))
                }
            }else{
                val result = categoryRepository.insertCategory(category)
                if(result > 0){
                    _state.update {
                        it.copy(showBottomSheet = false)
                    }
                    _state.update {
                        it.copy(categoryName = "")
                    }
                    sendEvent(UiEvent.ShowSnackBar(message = "Category saved Successfully"))
                }else{
                    sendEvent(UiEvent.ShowSnackBar(message = "Fail to save Category"))
                }
            }


        }
    }

    private fun categories(){
        viewModelScope.launch {
            categoryRepository.categories().onEach {categories ->
                _state.update {
                    it.copy(categories = categories.map { category-> category.toCategoryWithColor(useCase.color()) })
                }
            }.launchIn(viewModelScope)
        }
    }
    private fun selectCategory(category: CategoryWithColor){
        onEvent(InventoryEvent.ShowBottomSheet(true))
        _state.update {
            it.copy(selectedCategory = category)
        }
        _state.update {
            it.copy(categoryName = category.name)
        }
        _state.update {
            it.copy(selectedProductColor = category.color)
        }
    }
    private fun deleteCategory(category: CategoryWithColor){
       viewModelScope.launch {
           categoryRepository.deleteCategory(category = category.toCategory(useCase.color()))
       }
    }
    private fun selectedPage(index:Int){
        when(index){
            0 -> {
                _state.update {
                    it.copy(searchType = InventorySearchType.INVENTORY)
                }
            }
            1 -> {
                _state.update {
                    it.copy(searchType = InventorySearchType.CATEGORY)
                }
            }
            2 -> {
                _state.update {
                    it.copy(searchType = InventorySearchType.PURCHASE)
                }
            }
        }
    }
    private fun searchByName(name:String){
        _state.update {
            it.copy(searchText = name)
        }
        if(_state.value.searchText.isBlank()){
            when(_state.value.searchType){
                InventorySearchType.INVENTORY ->  onEvent(InventoryEvent.GetProduct)
                InventorySearchType.CATEGORY -> onEvent(InventoryEvent.GetAllCategories)
                InventorySearchType.PURCHASE -> {}
            }

        }else{
            when(_state.value.searchType){
                InventorySearchType.INVENTORY -> {
                    repository.searchProduct("*${_state.value.searchText}*").onEach {products ->
                        _state.update {
                            it.copy(products = products)
                        }
                    }.launchIn(viewModelScope)
                }
                InventorySearchType.CATEGORY -> {
                    categoryRepository.searchCategoryByName("*${_state.value.searchText}*").onEach {categories ->
                        _state.update {
                            it.copy(categories = categories.map { category ->
                                category.toCategoryWithColor(useCase.color())
                            })
                        }
                    }.launchIn(viewModelScope)
                }
                InventorySearchType.PURCHASE -> {}
            }

        }
    }

    private  fun sendEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}
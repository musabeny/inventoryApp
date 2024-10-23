package cashflow.presentation.cashFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashflow.data.mapper.toPurchaseDetail
import cashflow.data.mapper.toPurchaseGroupByItem
import cashflow.domain.enums.CashFlowTabs
import cashflow.domain.enums.IncomeExpenseType
import cashflow.domain.enums.ListViewType
import cashflow.domain.enums.UserFilterType
import cashflow.domain.model.FilterType
import cashflow.domain.model.IncomeExpense
import cashflow.domain.model.purchase.BillAndItems
import cashflow.domain.repository.CashFlowRepository
import cashflow.domain.repository.PurchaseRepository
import cashflow.domain.usecase.CashFlowUseCases
import cashflow.domain.usecase.PurchaseUseCase
import core.navigation.Routes
import core.util.BILL_ID
import core.util.CATEGORY_ID
import core.util.IS_INCOME_OR_EXPENSE
import core.util.UiEvent
import core.util.UiText
import database.model.CategoryIdAndIsIncomeOrExpense
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.all
import inventoryapp.composeapp.generated.resources.created
import inventoryapp.composeapp.generated.resources.draft
import inventoryapp.composeapp.generated.resources.enter_amount
import inventoryapp.composeapp.generated.resources.expense
import inventoryapp.composeapp.generated.resources.income
import inventoryapp.composeapp.generated.resources.selectCategory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.getString
import settings.data.mapper.toCategory
import settings.data.mapper.toCategoryWithColor
import settings.domain.repository.CategoryRepository
import settings.domain.useCase.ProductUseCase

class CashFlowViewModel(
    private val useCases: CashFlowUseCases,
    private val categoryRepository: CategoryRepository,
    private val productUseCase: ProductUseCase,
    private val cashFlowRepository: CashFlowRepository,
    private val purchaseRepository: PurchaseRepository,
    private val purchaseUseCase: PurchaseUseCase
):ViewModel() {
    private val _state = MutableStateFlow<CashFlowState>(CashFlowState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val now = Clock.System.todayIn(TimeZone.currentSystemDefault())


    init {
       onEvent(CashFlowEvent.ThisWeek)
       onEvent(CashFlowEvent.Categories)
       onEvent(CashFlowEvent.GetIncomeExpense)
       onEvent(CashFlowEvent.PurchasedItems)
       filterCategoryList()
    }

    fun onEvent(event: CashFlowEvent){
        when(event){
           is CashFlowEvent.GoToDateSelection ->{
                event.navController.navigate(Routes.Calender.route)
            }
           is CashFlowEvent.ThisWeek ->{
                val thisWeek = useCases.dateRange(now)
                _state.update {
                    it.copy(dateRange = thisWeek)
                }
            }
           is CashFlowEvent.Back ->{
               nextOrBack(false)

           }
           is CashFlowEvent.Next -> {
               nextOrBack(true)

           }
           is CashFlowEvent.SelectedDateRange ->{
               _state.update {
                   it.copy(dateRange = event.dateRange)
               }
               if(_state.value.selectedTabIndex == CashFlowTabs.INCOME){
                   onEvent(CashFlowEvent.GetIncomeExpense)
               }else{
                   onEvent(CashFlowEvent.PurchasedItems)
               }

           }
           is CashFlowEvent.Categories ->categories()
           is CashFlowEvent.ShowIncomeExpenseForm ->{
               _state.update {
                   it.copy(showIncomeOrExpenseForm = event.show)
               }
               _state.update {
                   it.copy(incomeExpenseType = event.incomeExpenseType)
               }
               if(!_state.value.showIncomeOrExpenseForm){
                   _state.update {
                       it.copy(amount = null)
                   }
                   _state.update {
                       it.copy(note = null)
                   }
                   _state.update {
                       it.copy(selectedCategory = null)
                   }
               }

               onEvent(CashFlowEvent.ToDaysDate)
           }
           is CashFlowEvent.ShowCategoryDropDown ->{
               _state.update {
                   it.copy(showCategoryDropDown = event.show)
               }
           }
           is CashFlowEvent.SelectedCategory ->{
                _state.update {
                    it.copy(selectedCategory = event.selected)
                }
                onEvent(CashFlowEvent.ShowCategoryDropDown(false))
            }
           is CashFlowEvent.EnterAmount ->{
               event.amount?.let {amount ->
                val result =   useCases.validateAmount(amount)
                   if(result || amount.isEmpty()){
                       _state.update {
                           it.copy(amount = event.amount)
                       }
                   }

               }



           }
           is CashFlowEvent.EnterNote ->{
               _state.update {
                   it.copy(note = event.note)
               }
           }
           is CashFlowEvent.ToDaysDate ->{
                val now = Clock.System.todayIn(TimeZone.currentSystemDefault())
                _state.update {
                    it.copy(today = "${now.dayOfMonth}/${now.monthNumber}/${now.year} ")
                }
            }
           is CashFlowEvent.SaveIncomeOrExpense ->saveIncomeExpense()
           is CashFlowEvent.GetIncomeExpense ->{
               _state.value.dateRange?.let {dateRange ->
                 val incomeExpense =  cashFlowRepository.getIncomeExpense(dateRange.start,dateRange.endInclusive)
                   incomeExpenseData(incomeExpense)

               }

            }
           is CashFlowEvent.ChangeViewType ->{
               _state.update {
                   it.copy(viewType = event.view)
               }
               if(_state.value.selectedTabIndex == CashFlowTabs.INCOME){
                   selectedFilteredIncomeExpense()
               }else{
                  onEvent(CashFlowEvent.PurchasedItems)
               }


           }
           is CashFlowEvent.ShowFilterSheet ->{
               _state.update {
                    it.copy(showFilterSheet = event.show)
                }

            }
           is CashFlowEvent.EntryTypes ->filters(UserFilterType.ENTRY)
           is CashFlowEvent.IncomeCategory ->filters(UserFilterType.INCOME)
           is CashFlowEvent.ExpenseCategory->filters(UserFilterType.EXPENSE)
           is CashFlowEvent.SelectedFilter -> selectedFilter(event.filterType,event.isChecked)
           is CashFlowEvent.SelectedFilterType ->{
               _state.update {
                   it.copy(userFilterType = event.filterType)
               }
           }
           is CashFlowEvent.ClearAllFilter ->filterCategoryList()
           is CashFlowEvent.DeleteDialog ->{
               _state.update {
                   it.copy(showDeleteDialog = event.show)
               }
               _state.update {
                   it.copy(selectedIncomeExpense = event.incomeExpense)
               }
           }
           is CashFlowEvent.DeleteIncomeExpense ->{
               viewModelScope.launch {
                  event.incomeExpense?.let {
                       cashFlowRepository.deleteIncomeExpense(it)
                  }
                  onEvent(CashFlowEvent.DeleteDialog(false,null))
               }
           }
           is CashFlowEvent.GoToBreakDownPage ->{
               event.navController.navigate(Routes.BreakDown.route.replace("{$CATEGORY_ID}","${event.categoryId}").replace("{$IS_INCOME_OR_EXPENSE}","${event.incomeOrExpense}"))
           }
           is CashFlowEvent.DeleteCategoryWithItems ->{
               viewModelScope.launch {
                   val categoryIdAndIsIncomeOrExpense = CategoryIdAndIsIncomeOrExpense(event.categoryId,event.isIncomeOrExpense)
                  cashFlowRepository.deleteByCategoryIdAndIsIncomeOrExpense(categoryIdAndIsIncomeOrExpense)
                   onEvent(CashFlowEvent.DeleteDialog(false,null))
               }

           }
            is CashFlowEvent.GoToPurchase ->{
                event.navController.navigate(Routes.Purchase.route)
            }
            is CashFlowEvent.PurchasedItems -> purchasedItems()
            is CashFlowEvent.SelectedPageIndex ->{
                _state.update {
                    it.copy(selectedTabIndex = if(event.index == CashFlowTabs.INCOME.index)CashFlowTabs.INCOME else CashFlowTabs.PURCHASE)
                }
                _state.update {
                    it.copy(userFilterType = if(event.index == CashFlowTabs.INCOME.index) UserFilterType.ENTRY else UserFilterType.MEMBERS )
                }

                if(_state.value.selectedTabIndex == CashFlowTabs.INCOME){
                    selectedFilteredIncomeExpense()
                }else{
                    onEvent(CashFlowEvent.PurchasedItems)
                }
            }
            is CashFlowEvent.MemberCategory ->filters(UserFilterType.MEMBERS)
            is CashFlowEvent.StatusCategory ->filters(UserFilterType.STATUS)
            is CashFlowEvent.Search ->{
                if(_state.value.selectedTabIndex == CashFlowTabs.PURCHASE){
                    searchPurchaseItem(event.text)
                }else{
                    searchIncomeExpense(event.text)
                }

            }
            is CashFlowEvent.ClearSearchText ->{
                _state.update {
                    it.copy(searchText = "")
                }
                if(_state.value.selectedTabIndex == CashFlowTabs.INCOME){
                   onEvent(CashFlowEvent.GetIncomeExpense)
                }else{
                    onEvent(CashFlowEvent.PurchasedItems)
                }
            }
            is CashFlowEvent.NavigateToPurchaseReceipt ->{
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(Routes.PurchaseReceipt.route.replace("{$BILL_ID}","${event.billId}")))
                }

            }
           else -> {}
        }
    }

   private fun categories(){
       viewModelScope.launch {
           categoryRepository.categories().onEach { categories ->
               _state.update {
                   it.copy(categories = categories.map {category -> category.toCategoryWithColor(productUseCase.color()) })
               }
           }.launchIn(viewModelScope)

       }


   }

   private fun saveIncomeExpense(){
       viewModelScope.launch {
           val category  = _state.value.selectedCategory
           val amount = _state.value.amount
           if( category== null){
               sendEvent(UiEvent.ShowSnackBar(UiText.StringResources(Res.string.selectCategory)))
               return@launch
           }
           if(amount.isNullOrBlank()){
               sendEvent(UiEvent.ShowSnackBar(UiText.StringResources(Res.string.enter_amount)))
               return@launch
           }
           val amountInNumber = amount.toDoubleOrNull()
           if(amountInNumber == null){
               sendEvent(UiEvent.ShowSnackBar(UiText.DynamicText("Please enter a proper number")))
               return@launch
           }
           val incomeExpense  = IncomeExpense(
               id = null,
               category = category.toCategory(productUseCase.color()).toCategoryWithColor(productUseCase.color()),
               amount = amountInNumber,
               note = _state.value.note,
               isIncomeOrExpense = _state.value.incomeExpenseType.value,
               dateCreated = Clock.System.todayIn(TimeZone.currentSystemDefault())
           )
         val result =  cashFlowRepository.insertIncomeExpense(incomeExpense)
           if(result<=0){
               sendEvent(UiEvent.ShowSnackBar(UiText.DynamicText("Fail to save data")))
           }else{
               onEvent(CashFlowEvent.ShowIncomeExpenseForm(false,_state.value.incomeExpenseType))
           }
       }
   }


    private fun nextOrBack(isNext:Boolean){
        val dateRange = state.value.dateRange
        if(dateRange?.start != dateRange?.endInclusive ){
            _state.update {
                it.copy(dateRange = now..now)
            }
        }else{
           val localDate =   if(isNext){
                _state.value.dateRange?.endInclusive?.plus(DatePeriod(days = 1))
            }else{
                _state.value.dateRange?.endInclusive?.minus(DatePeriod(days = 1))
            }
            localDate?.let {
                _state.update {
                    it.copy(dateRange = localDate .. localDate)
                }
            }

        }
        if((_state.value.dateRange?.endInclusive ?: now) >= now){
            _state.update {
                it.copy(showNextArrow = false)
            }
        }else{
            _state.update {
                it.copy(showNextArrow = true)
            }
        }

      onEvent(CashFlowEvent.GetIncomeExpense)
    }



    private fun filters(filter: UserFilterType){
        viewModelScope.launch {
            when(filter){
                UserFilterType.ENTRY -> {
                    val entryTypes = listOf(
                        FilterType(0,getString(Res.string.all)),
                        FilterType(IncomeExpenseType.INCOME.value.toLong(),getString(Res.string.income)),
                        FilterType(IncomeExpenseType.EXPENSE.value.toLong(),getString( Res.string.expense))
                    )
                    _state.update {
                        it.copy(entryType = entryTypes)
                    }
                }
                UserFilterType.MEMBERS -> {
                    val members = listOf(
                        FilterType(0,getString(Res.string.all)),
                        FilterType(1,"musabeni14@gmail.com"),
                    )
                    _state.update {
                        it.copy(members = members)
                    }
                }
                UserFilterType.INCOME -> getCategories(IncomeExpenseType.INCOME.value)
                UserFilterType.EXPENSE -> getCategories(IncomeExpenseType.EXPENSE.value)
                UserFilterType.STATUS ->{
                    val status = listOf(
                        FilterType(0,getString(Res.string.draft)),
                        FilterType(1,getString(Res.string.created)),
                    )
                    _state.update {
                        it.copy(status = status)
                    }
                }
            }
        }
    }

    private fun getCategories(type:Int){
        cashFlowRepository.getCategoryByIncomeOrExpense(type ).onEach { filter ->
           if(type == 1){
               _state.update {
                   it.copy(incomeCategory = filter)
               }
           }else{
               _state.update {
                   it.copy(expenseCategory = filter)
               }
           }

        }.launchIn(viewModelScope)
    }

    private fun selectedFilter(selectedFilter: FilterType, isChecked:Boolean){
        when(_state.value.userFilterType){
            UserFilterType.ENTRY ->{
                _state.update {
                    it.copy(entryType = _state.value.entryType.map {filter -> if(filter == selectedFilter) filter.copy(isChecked = isChecked) else filter })
                }
            }
            UserFilterType.MEMBERS -> {
                _state.update {
                    it.copy(members = _state.value.members.map {filter -> if(filter == selectedFilter) filter.copy(isChecked = isChecked) else filter })
                }
            }
            UserFilterType.INCOME -> {
                _state.update {
                    it.copy(incomeCategory = _state.value.incomeCategory.map {filter -> if(filter == selectedFilter) filter.copy(isChecked = isChecked) else filter })
                }
            }
            UserFilterType.EXPENSE -> {
                _state.update {
                    it.copy(expenseCategory = _state.value.expenseCategory.map {filter -> if(filter == selectedFilter) filter.copy(isChecked = isChecked) else filter })
                }
            }
            UserFilterType.STATUS ->{
                _state.update {
                    it.copy(status = _state.value.status.map {filter -> if(filter == selectedFilter) filter.copy(isChecked = isChecked) else filter })
                }
            }
        }
        if(_state.value.selectedTabIndex == CashFlowTabs.INCOME){
            selectedFilteredIncomeExpense()
        }else{
            purchasedItems()
        }

    }
    private fun selectedFilteredIncomeExpense(){
        _state.value.dateRange?.let {dateRange ->
            val filteredIncomeExpense = cashFlowRepository.filterIncomeExpense(
                startDate = dateRange.start,
                endDate = dateRange.endInclusive,
                entryTypes = _state.value.entryType.filter { it.id != 0L && it.isChecked }.map { it.id.toInt() },
                category = _state.value.incomeCategory.filter {it.isChecked  }.map { it.id } .union(_state.value.expenseCategory.filter {it.isChecked  }.map { it.id }).toList(),
                categoryIncomeExpenseType = listOf(
                    if (_state.value.incomeCategory.any { it.isChecked }) IncomeExpenseType.INCOME.value else 0,
                    if (_state.value.expenseCategory.any { it.isChecked }) IncomeExpenseType.EXPENSE.value else 0
                ).filter { it != 0 }
            )

            incomeExpenseData(filteredIncomeExpense)
        }
    }

    private fun filterCategoryList(){
        onEvent(CashFlowEvent.EntryTypes)
        onEvent(CashFlowEvent.IncomeCategory)
        onEvent(CashFlowEvent.ExpenseCategory)
        onEvent(CashFlowEvent.GetIncomeExpense)
        onEvent(CashFlowEvent.MemberCategory)
        onEvent(CashFlowEvent.StatusCategory)
        onEvent(CashFlowEvent.ShowFilterSheet(false))
    }

    private fun incomeExpenseData( incomeExpense: Flow<List<IncomeExpense>>){

        incomeExpense.onEach { incomeExpenses ->
            _state.update {
                it.copy(totalExpense = "${incomeExpenses.filter { it.isIncomeOrExpense == IncomeExpenseType.EXPENSE.value }.sumOf { it.amount }}")
            }
            _state.update {
                it.copy(totalIncome = "${incomeExpenses.filter { it.isIncomeOrExpense == IncomeExpenseType.INCOME.value }.sumOf { it.amount }}")
            }

            if(_state.value.viewType == ListViewType.LIST){
                _state.update {
                    it.copy(incomeExpensesGroup = mapOf())
                }
                _state.update {
                    it.copy(incomeExpenses = incomeExpenses.groupBy {result -> result.dateCreated })
                }

            }else{
                _state.update {
                    it.copy(incomeExpenses = mapOf())
                }
                _state.update {
                    it.copy(incomeExpensesGroup = incomeExpenses.groupBy {res -> Pair(res.isIncomeOrExpense,res.category)})
                }
            }



        } .launchIn(viewModelScope)
    }
    private fun purchasedItems(){
        if(_state.value.status.any { it.isChecked }){
            filteredPurchasedItem()
        }else{
            nonFilteredPurchasedItem()
        }
    }
    private fun filteredPurchasedItem(){
        _state.value.dateRange?.let {dateRange ->
            val filteredPurchasedItem = purchaseRepository.purchasesFiltered(dateRange.start,dateRange.endInclusive,_state.value.status.filter { it.isChecked }.map { if(it.id == 1L) it.copy(isChecked = false) else it }.map { it.isChecked })
             getPurchasedItem(filteredPurchasedItem)
        }

    }
    private fun nonFilteredPurchasedItem(){
        state.value.dateRange?.let {dateRange ->
            val purchasedItem =   purchaseRepository.purchases(dateRange.start,dateRange.endInclusive)
            getPurchasedItem(purchasedItem)
        }
    }
    private fun getPurchasedItem(purchasedItem: Flow<List<BillAndItems>>){
        purchasedItem.onEach {purchaseItems ->
            _state.update {
                it.copy(purchaseTotal = "${purchaseItems.map { it.items.sumOf {item-> item.price } }.sumOf { it }}")
            }
            if(_state.value.viewType == ListViewType.LIST){
                _state.update {
                    it.copy(purchaseGroupedByItem = mapOf())
                }
                _state.update {
                    it.copy(purchaseByDate = purchaseItems.map { it.toPurchaseDetail() }.groupBy { it.dateCreated })
                }
            }else{
                _state.update {
                    it.copy(purchaseByDate = mapOf())
                }
                val purchased =  purchaseItems.map { it.items.map { it.toPurchaseGroupByItem(purchaseUseCase) } }.flatten().groupBy { it.name }
                _state.update {
                    it.copy(purchaseGroupedByItem = purchased)
                }
            }


        }.launchIn(viewModelScope)
    }

    private fun searchPurchaseItem(searchText:String){
        viewModelScope.launch {
            _state.update {
                it.copy(searchText = searchText)
            }
            if(_state.value.searchText.isEmpty()){
                nonFilteredPurchasedItem()
            }else{
                _state.value.dateRange?.let {dateRange ->

                    val result = if(_state.value.viewType == ListViewType.GROUP)
                        purchaseRepository.searchedByItemName(dateRange.start,dateRange.endInclusive,"*${_state.value.searchText}*")
                    else
                        purchaseRepository.searchedByBillOrNote(dateRange.start,dateRange.endInclusive,"*${_state.value.searchText}*")
                    getPurchasedItem(result)
                }
            }

        }
    }
    private fun searchIncomeExpense(searchText:String){
        viewModelScope.launch {
            _state.update {
                it.copy(searchText = searchText)
            }
            if (_state.value.searchText.isEmpty()){
                 onEvent(CashFlowEvent.GetIncomeExpense)
            }else{
                _state.value.dateRange?.let {dateRange ->
                    val filteredExpenseIncome =if(_state.value.viewType == ListViewType.LIST){
                        cashFlowRepository.searchedByCategoryOrNote(dateRange.start,dateRange.endInclusive,"*${_state.value.searchText}*")
                    }else{
                        cashFlowRepository.searchedByCategory(dateRange.start,dateRange.endInclusive,"*${_state.value.searchText}*")
                    }
                    incomeExpenseData(filteredExpenseIncome)
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
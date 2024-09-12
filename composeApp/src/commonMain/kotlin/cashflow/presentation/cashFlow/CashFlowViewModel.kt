package cashflow.presentation.cashFlow

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashflow.domain.enums.IncomeExpenseType
import cashflow.domain.enums.ListViewType
import cashflow.domain.enums.UserFilterType
import cashflow.domain.model.FilterType
import cashflow.domain.model.IncomeExpense
import cashflow.domain.repository.CashFlowRepository
import cashflow.domain.usecase.CashFlowUseCases
import core.navigation.Routes
import core.util.UiEvent
import core.util.UiText
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.all
import inventoryapp.composeapp.generated.resources.expense
import inventoryapp.composeapp.generated.resources.income
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
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
    private val cashFlowRepository: CashFlowRepository
):ViewModel() {
    private val _state = MutableStateFlow<CashFlowState>(CashFlowState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private  val incomeExpenseTest = MutableStateFlow<List<IncomeExpense>>(emptyList())

    private val now = Clock.System.todayIn(TimeZone.currentSystemDefault())
    private val initDateRange = useCases.dateRange(now)
    private val _incomeExpenseData =
//        .getIncomeExpense(_state.value.dateRange?.start ?:initDateRange.start,_state.value.dateRange?.endInclusive?:initDateRange.endInclusive )
    incomeExpenseTest
        .map {incomeExpenseList ->
            println("incomeExpense in filter ${incomeExpenseList.size}")
            if(
                _state.value.incomeCategory.map { it.isChecked }.contains(true) ||
                _state.value.expenseCategory.map { it.isChecked }.contains(true)||
                _state.value.entryType.filter { it.id != 0L }.map { it.isChecked }.contains(true)
            )

                incomeExpenseList.filter { incomeExpense ->
                    (incomeExpense.category.id in _state.value.incomeCategory.filter { income -> income.isChecked }.map { it.id }) ||
                            (incomeExpense.category.id in _state.value.expenseCategory.filter { expense -> expense.isChecked }.map { it.id }) ||
                            (incomeExpense.isIncomeOrExpense.toLong() in _state.value.entryType.filter { entry -> entry.isChecked && entry.id != 0L }.map { it.id })

                }
            else incomeExpenseList
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    init {
       onEvent(CashFlowEvent.ThisWeek)
       onEvent(CashFlowEvent.Categories)
       onEvent(CashFlowEvent.GetIncomeExpense)
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
               onEvent(CashFlowEvent.GetIncomeExpense)
           }
           is CashFlowEvent.Categories ->categories()
           is CashFlowEvent.ShowIncomeExpenseForm ->{
               _state.update {
                   it.copy(showIncomeForm = event.show)
               }
               _state.update {
                   it.copy(incomeExpenseType = event.incomeExpenseType)
               }
               if(!_state.value.showIncomeForm){
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
                   it.copy(vewType = event.view)
               }
               selectedFilteredIncomeExpense()

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
               sendEvent(UiEvent.ShowSnackBar(UiText.DynamicText("Please select category").value))
               return@launch
           }
           if(amount.isNullOrBlank()){
               sendEvent(UiEvent.ShowSnackBar(UiText.DynamicText("Amount can not be empty").value))
               return@launch
           }
           val amountInNumber = amount.toDoubleOrNull()
           if(amountInNumber == null){
               sendEvent(UiEvent.ShowSnackBar(UiText.DynamicText("Please enter a proper number").value))
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
               sendEvent(UiEvent.ShowSnackBar(UiText.DynamicText("Fail to save data").value))
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

    private  fun sendEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
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
                UserFilterType.MEMBERS -> {}
                UserFilterType.INCOME -> getCategories(IncomeExpenseType.INCOME.value)
                UserFilterType.EXPENSE -> getCategories(IncomeExpenseType.EXPENSE.value)
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
        }
        selectedFilteredIncomeExpense()
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

            if(_state.value.vewType == ListViewType.LIST){
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


}
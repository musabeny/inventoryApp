package cashflow.presentation.breakDown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashflow.domain.repository.CashFlowRepository
import cashflow.presentation.cashFlow.CashFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class BreakDownViewModel(
 private  val repository: CashFlowRepository
):ViewModel(){

    private val _state = MutableStateFlow(BreakDownState())
    val state = _state.asStateFlow()

    fun onEvent(event: BreakDownEvent){
        when(event){
            is BreakDownEvent.GetIncomeOrExpense ->{
              if(event.categoryId != null && event.incomeOrExpense != null){
                  repository.getIncomeExpenseByCategoryId(event.categoryId,event.incomeOrExpense).onEach {incomeExpenses ->
                      _state.update {
                          it.copy(amount = incomeExpenses.sumOf { it.amount })
                      }
                      _state.update {
                          it.copy(category = incomeExpenses.firstOrNull()?.category )
                      }
                      _state.update {
                          it.copy(incomeExpenses = incomeExpenses.sortedByDescending { sort -> sort.dateCreated }.groupBy {result -> result.dateCreated })
                      }
                      state.value.category?.let {category ->
                          event.onEventCashFlow(CashFlowEvent.SelectedCategory(category))
                      }

                  }.launchIn(viewModelScope)

              }
            }
            is BreakDownEvent.CloseScreen ->{
                event.navController.navigateUp()
            }
        }
    }
}
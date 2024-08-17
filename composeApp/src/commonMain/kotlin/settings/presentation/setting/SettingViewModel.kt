package settings.presentation.setting

import androidx.lifecycle.ViewModel
import core.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import settings.domain.model.SettingMenu

class SettingViewModel(
    private val hello:String
) : ViewModel(){
    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()
    private val settings = listOf(
        SettingMenu(1,"Manage Inventory",Routes.Inventory.route),
        SettingMenu(1,"Staff Management",Routes.Sales.route),
        SettingMenu(1,"Language Settings",Routes.CashFlow.route),
        SettingMenu(1,"Payment Settings",Routes.Settings.route)
    )
    init {
        onEvent(SettingEvent.GetMenu)
        println("data printed $hello")
    }

    fun onEvent(event: SettingEvent){
        when(event){
           is SettingEvent.GetMenu -> {
                _state.update {
                    it.copy(settings = settings)
                }
            }
           is SettingEvent.NavigateTo ->{
                event.navController.navigate(event.route)
            }
            else ->{}
        }
    }
}
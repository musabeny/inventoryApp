package settings.presentation.setting

import androidx.navigation.NavHostController

sealed interface SettingEvent {
    data object GetMenu:SettingEvent
    data class NavigateTo(val navController: NavHostController,val route:String):SettingEvent
}
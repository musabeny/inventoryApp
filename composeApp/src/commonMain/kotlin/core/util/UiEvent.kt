package core.util

import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.StringResource

sealed interface UiEvent{
    data object PopBackStack:UiEvent
    data class Navigate(val route:String):UiEvent
    data class ShowSnackBar(val message: String):UiEvent
}
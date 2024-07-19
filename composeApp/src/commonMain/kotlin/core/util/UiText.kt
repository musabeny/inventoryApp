package core.util

import androidx.compose.runtime.Composable
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.can_not_be_empty
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface UiText{
    data class DynamicText(val value:String):UiText
     class StringResources(
         val res:StringResource
     ):UiText

    @Composable
    fun asString():String{
       return when(this){
           is DynamicText -> value
           is StringResources -> stringResource(resource = res)
       }
    }





}

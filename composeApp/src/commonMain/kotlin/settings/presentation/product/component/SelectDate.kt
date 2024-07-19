package settings.presentation.product.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import settings.presentation.product.ProductEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDate(
    modifier: Modifier,
    showDatePickerDialog:Boolean = false,
    onEvent: (ProductEvent) -> Unit
){
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    if(showDatePickerDialog){
        DatePickerDialog(
            onDismissRequest = {
                onEvent(ProductEvent.DatePickerDialog(show = false))
            },
            modifier = modifier,
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(ProductEvent.SelectedDate(datePickerState.selectedDateMillis))
                    }
                ){
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onEvent(ProductEvent.DatePickerDialog(show = false))
                    }
                ){
                    Text("Cancel")
                }
            }
        ){
            DatePicker(
                state = datePickerState
            )
        }
    }

}
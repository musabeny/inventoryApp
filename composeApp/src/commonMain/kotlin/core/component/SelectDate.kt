package core.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDate(
    modifier: Modifier,
    showDatePickerDialog:Boolean = false,
    onDismiss:() -> Unit,
    onSelectDate:(datePickerState: DatePickerState) -> Unit
){
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    if(showDatePickerDialog){
        DatePickerDialog(
            onDismissRequest = {
                onDismiss()
            },
            modifier = modifier,
            confirmButton = {
                TextButton(
                    onClick = {
                        onSelectDate(datePickerState)
                    }
                ){
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
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
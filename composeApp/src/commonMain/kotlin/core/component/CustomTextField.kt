package core.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomTextField(
    modifier: Modifier,
    onValue:(String) -> Unit = {},
    clearText:() -> Unit = {},
    errorMessage: StringResource?,
    value:String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    showTrailingIcon:Boolean = false
){
    TextField(
        modifier = modifier,
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
//            errorContainerColor = Color.Transparent,
//            cursorColor = Color.Transparent,
//            errorCursorColor = Color.Transparent
        ),
        trailingIcon = {
           if(showTrailingIcon){
               Icon(
                 modifier = Modifier.clickable {
                     clearText()
                 },
                 imageVector = Icons.Filled.Clear,
                 contentDescription = "Clear icon",
                  tint = MaterialTheme.colorScheme.primary
               )
           }
        },
        value = value,
        textStyle = TextStyle(
            background = MaterialTheme.colorScheme.onPrimary,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        onValueChange = onValue,
        supportingText = {
            AnimatedVisibility(visible = errorMessage != null){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if(errorMessage != null) stringResource(errorMessage) else "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.End
                )
            }

        },
        isError = errorMessage != null,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}
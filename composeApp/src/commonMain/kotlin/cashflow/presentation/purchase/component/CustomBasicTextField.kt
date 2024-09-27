package cashflow.presentation.purchase.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomBasicTextField(
    modifier: Modifier,
    label:StringResource,
    value:String,
    showHint:Boolean = true,
    onValueChange:(String) -> Unit,
    keyboardType:KeyboardType = KeyboardType.Text,
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            decorationBox = {innerTextField ->
                Box{
                    innerTextField()
                    if(value.isEmpty() && showHint){
                        Text(
                            text = stringResource(label),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                }

            }
        )


    }

}
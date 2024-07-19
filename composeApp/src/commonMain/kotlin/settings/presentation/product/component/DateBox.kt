package settings.presentation.product.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import core.component.CustomTextField
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import settings.presentation.product.ProductEvent

@Composable
fun DateBox(
    modifier: Modifier,
    label: StringResource,
    isExpireDate:Boolean = true,
    selectedDate:String?  = null,
    onEvent: (ProductEvent) -> Unit,
    value:String = ""
){
    Surface(
        modifier = modifier,
        shadowElevation = 3.dp,
        shape = RoundedCornerShape(2.dp),
        color = MaterialTheme.colorScheme.onPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(label),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelMedium
            )
            if(isExpireDate){
                Text(
                    text = selectedDate ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }else{
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValue ={
                        onEvent(ProductEvent.EnterExpireDateAlert(it))
                    },
                    errorMessage = null,
                    value = value,
                    keyboardType = KeyboardType.Number
                )
            }
        }
    }
}
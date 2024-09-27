package cashflow.presentation.purchase.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun InputAndTitle(
    modifier: Modifier,
    label: StringResource,
    value:String,
    error:String? = null,
    onValueChange:(String) -> Unit,

){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            shadowElevation = 2.dp,
            color = MaterialTheme.colorScheme.onPrimary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( 8.dp),
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(label),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
                CustomBasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = label,
                    value = value,
                    showHint = false,
                    onValueChange = onValueChange,
                )
            }

        }
        error?.let {message ->
            Text(
                text = message,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }


}
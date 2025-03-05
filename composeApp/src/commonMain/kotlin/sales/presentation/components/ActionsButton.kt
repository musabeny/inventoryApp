package sales.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import sales.presentation.payment.component.ActionBtn

@Composable
fun ActionsButton(
    btnOne:() -> Unit,
    btnTwo:() -> Unit,
    labelOne:StringResource,
    labelTwo:StringResource
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionBtn(
            modifier = Modifier.weight(1f),
            label = labelOne,
            textColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.onPrimary,
            onClick = btnOne
        )
        ActionBtn(
            modifier = Modifier.weight(1f),
            label = labelTwo,
            textColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = btnTwo
        )

    }
}
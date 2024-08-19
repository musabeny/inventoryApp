package cashflow.presentation.cashFlow.component

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CashFlowButton(
    modifier: Modifier,
    color: Color,
    label:StringResource,
    onClick:()-> Unit,
){
    Button(
        modifier = modifier,
       onClick = onClick,
       colors = ButtonDefaults.buttonColors().copy(
           containerColor = color,
           contentColor = MaterialTheme.colorScheme.onPrimary
       )
    ){
       Text(
           text = stringResource(label),
           style = MaterialTheme.typography.bodyMedium,
           fontWeight = FontWeight.Bold
       )
    }
}
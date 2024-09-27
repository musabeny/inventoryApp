package cashflow.presentation.purchase.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_new_item
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun PurchaseButton(
    modifier: Modifier = Modifier.padding(8.dp),
    containerColor: Color = MaterialTheme.colorScheme.tertiary,
    textColor: Color = MaterialTheme.colorScheme.onTertiary,
    label: StringResource = Res.string.add_new_item,
    onClick:() -> Unit
){
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = containerColor
        )
    ){
        Text(
            text = stringResource(label),
            color =  textColor
        )
    }
}
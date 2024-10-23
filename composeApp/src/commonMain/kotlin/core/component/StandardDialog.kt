package core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cashflow.presentation.cashFlow.component.DeleteDialog
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.no
import inventoryapp.composeapp.generated.resources.yes
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardDialog(
    modifier: Modifier,
    message:String,
    onConfirm:()-> Unit,
    onCancel:()->Unit,
    showDialog:Boolean
){
    if(showDialog){
        BasicAlertDialog(
            modifier = modifier,
            onDismissRequest = onCancel
        ){
            Card(
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onCancel
                        ){
                            Text(text = stringResource(Res.string.no).uppercase())
                        }
                        TextButton(
                            onClick = onConfirm
                        ){
                            Text(text = stringResource(Res.string.yes).uppercase())
                        }
                    }
                }
            }
        }
    }
}
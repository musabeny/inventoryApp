package sales.presentation.editItem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_new_item
import inventoryapp.composeapp.generated.resources.save
import inventoryapp.composeapp.generated.resources.save_for_later
import sales.presentation.payment.component.ActionBtn

@Composable
fun ActionsBtn(
    addNewItem:()->Unit,
    save:()-> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionBtn(
            modifier = Modifier.weight(1f),
            label = Res.string.add_new_item,
            textColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.onPrimary,
            onClick = addNewItem
        )
        ActionBtn(
            modifier = Modifier.weight(1f),
            label = Res.string.save,
            textColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = save
        )

    }
}
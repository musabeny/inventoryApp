package sales.presentation.editItem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.edit_items
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemTopBar(
   onClose:()-> Unit
){
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        title = {
            Text(
                text = stringResource(Res.string.edit_items),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
       actions = {
           Icon(
               modifier = Modifier.clickable {
                   onClose()
               },
               imageVector = Icons.Filled.Close,
               contentDescription = null,
               tint = MaterialTheme.colorScheme.primary
           )
       }
    )
}
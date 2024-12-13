package sales.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSheet(
    modifier: Modifier
){
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {}
    ){
      Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.End
      ) {
         Icon(
             imageVector = Icons.Filled.Close,
             contentDescription = "Close Icon"
         )
      }
    }
}

@Composable
fun ProductRow(
    modifier: Modifier,
    productName:String,
    price:String,
    count:String
){
    Row(
        modifier = modifier
    ) {
     Column(
         modifier = Modifier
     ) {
         Text(
             text = productName,
             style = MaterialTheme.typography.titleLarge
         )
         Text(text = "$price x ")
     }
    }
}
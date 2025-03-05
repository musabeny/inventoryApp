package sales.presentation.payment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import sales.domain.model.ItemDetail

@Composable
fun ItemsSection(
    modifier: Modifier,
    items:List<ItemDetail>
){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
           items(items){item ->
               Item(
                  modifier = Modifier
                      .fillMaxWidth(),
                  itemName = item.product ?: "",
                  expression = item.expression ?: "",
                  total = if(item.total == null)  "" else "${item.total}"
              )
           }
        }
    }

}
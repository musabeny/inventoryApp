package sales.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.barcode
import inventoryapp.composeapp.generated.resources.item
import inventoryapp.composeapp.generated.resources.save
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActionIcons(
    item:String,
    itemCount:Int
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SaleIconButton(){}
        Text(
            modifier = Modifier.weight(1f),
            text = if(item.isEmpty()) "" else stringResource(Res.string.item,item,itemCount),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End
            )
        )
        SaleIconButton(
            icon = Res.drawable.save
        ){}
    }
}

@Composable
fun SaleIconButton(
    modifier: Modifier = Modifier,
    icon: DrawableResource = Res.drawable.barcode,
    onClick:()->Unit,
){
    IconButton(
        modifier = modifier,
        onClick = onClick
    ){
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(2.dp)
                )
        ){
            Icon(
                modifier = Modifier.padding(8.dp),
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
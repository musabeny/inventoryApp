package settings.presentation.inventory.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add
import inventoryapp.composeapp.generated.resources.btn_add
import inventoryapp.composeapp.generated.resources.btn_sort
import inventoryapp.composeapp.generated.resources.expire_date_reminder
import inventoryapp.composeapp.generated.resources.items_about_to_expire
import inventoryapp.composeapp.generated.resources.show_only
import inventoryapp.composeapp.generated.resources.sort
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import settings.data.mapper.toLocalDate
import settings.domain.model.product.Product
import settings.presentation.inventory.InventoryEvent

@Composable
fun InventoryTab(
    modifier: Modifier,
    onEvent: (InventoryEvent)-> Unit,
    navController: NavHostController,
    products:List<Product>,
    showOnlyExpired:Boolean
){
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                modifier = Modifier,
                checked = showOnlyExpired,
                onCheckedChange = {
                    onEvent(InventoryEvent.ShowOnlyExpired)
                }
            )
            Text(
                text = stringResource(Res.string.show_only),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(Res.string.items_about_to_expire),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline
        )
       Box(modifier = Modifier
           .weight(1f)
           .padding(bottom = 8.dp)
           .padding(horizontal = 8.dp),
           contentAlignment = Alignment.Center
       ){
           LazyColumn(
               modifier = Modifier.fillMaxSize(),
               verticalArrangement = Arrangement.spacedBy(8.dp)
           ) {
               items(products){product ->
                   ProductItem(
                       modifier = Modifier
                           .fillMaxWidth()
                           .height(50.dp)
                           .clickable {
                               onEvent(InventoryEvent.SelectProduct(navController,product.id ?: 0))
                           },
                       product = product

                   )
               }
           }
       }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            BottomButton(
                label = Res.string.btn_sort,
                icon = Res.drawable.sort
            ){

            }


            BottomButton(
                label = Res.string.btn_add,
                icon = Res.drawable.add
            ){
                onEvent(InventoryEvent.GoToAddItem(navController))
            }


        }

    }
}

@Composable
fun RowScope.BottomButton(
    label:StringResource,
    icon:DrawableResource,
    onClick:() -> Unit = {}
){
    IconButton(
        modifier = Modifier.weight(1f),
        onClick = onClick
    ){
        Row {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = stringResource(label),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ProductItem(
    modifier: Modifier,
    product: Product,
){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(product.color.color),
                contentAlignment = Alignment.Center
            ) {
               Text(
                   text = product.code,
                   color = Color.White,
                   fontWeight = FontWeight.Bold
               )
            }

            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = product.name,
                    fontWeight = FontWeight.Bold
                )
                if(product.expireDate != null){
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(Res.string.expire_date_reminder,product.expireDate.toLocalDate()),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }


            Text(
                modifier = Modifier.weight(1f),
                text = if(product.price != null) "${product.price}" else "?",
                fontWeight = FontWeight.SemiBold,
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
        }
    }
}

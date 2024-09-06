package core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.check_circle
import inventoryapp.composeapp.generated.resources.choose_color
import inventoryapp.composeapp.generated.resources.enter_category_name
import inventoryapp.composeapp.generated.resources.save
import inventoryapp.composeapp.generated.resources.update
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import settings.domain.model.category.CategoryWithColor
import settings.domain.model.product.ProductColor
import settings.presentation.inventory.InventoryEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategory(
    showBottomSheet:Boolean,
    onEvent: (InventoryEvent)->Unit,
    colors: List<ProductColor>,
    selectedProductColor: ProductColor?,
    categoryName: String,
    categoryError: StringResource?,
    category: CategoryWithColor?

) {
    if (showBottomSheet) {
        ModalBottomSheet(
            dragHandle = null,
            onDismissRequest = {
                onEvent(InventoryEvent.ShowBottomSheet(false))
            }
        ) {
            CreateCategories(
                colors = colors,
                onEvent = onEvent,
                selectedProductColor = selectedProductColor,
                categoryName = categoryName,
                categoryError = categoryError,
                category = category
            )
        }
    }
}




 @Composable
fun CreateCategories(
        colors: List<ProductColor>,
        onEvent: (InventoryEvent) -> Unit,
        selectedProductColor: ProductColor?,
        categoryName: String,
        categoryError: StringResource?,
        category: CategoryWithColor?
    ) {
     Column(
         modifier = Modifier.padding(8.dp)
     ) {

         CustomTextField(
             modifier = Modifier
                 .fillMaxWidth(),
             onValue = {
                 onEvent(InventoryEvent.EnterCategoryName(it))
             },
             value = categoryName,
             errorMessage = categoryError,
             hint = Res.string.enter_category_name
         )
         Text(
             text = stringResource(Res.string.choose_color),
             style = MaterialTheme.typography.labelSmall,
             color = Color.Gray
         )

         LazyRow(
             modifier = Modifier.fillMaxWidth(),
             horizontalArrangement = Arrangement.SpaceAround
         ) {
             items(colors) { productColor ->
                 Box(
                     modifier = Modifier
                         .size(48.dp)
                         .background(productColor.color, shape = RoundedCornerShape(8.dp))
                         .clickable {
                             onEvent(InventoryEvent.SelectColor(productColor))
                         },
                     contentAlignment = Alignment.Center
                 ) {
                     Icon(
                         modifier = Modifier.size(40.dp),
                         painter = painterResource(Res.drawable.check_circle),
                         contentDescription = "check mark",
                         tint = Color.White.copy(alpha = if (productColor == selectedProductColor) 1f else 0f)
                     )
                 }
             }
         }

         Button(
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(top = 8.dp),
             onClick = {
                 onEvent(InventoryEvent.SaveCategories)
             },
             shape = RoundedCornerShape(8.dp)
         ) {
             Text(text = stringResource(if (category == null) Res.string.save else Res.string.update))
         }

     }
    }





package settings.presentation.inventory.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.component.AddCategory
import core.component.CustomTextField
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
fun CategoriesTab(
    modifier: Modifier,
    onEvent: (InventoryEvent)->Unit,
    showBottomSheet:Boolean,
    colors:List<ProductColor>,
    selectedProductColor: ProductColor?,
    categoryName:String,
    categoryError: StringResource?,
    categories:List<CategoryWithColor>,
    category: CategoryWithColor?

){
   Scaffold(
       modifier =modifier,
       floatingActionButton = {
           ExtendedFloatingActionButton(
               onClick = {
                   onEvent(InventoryEvent.ShowBottomSheet(true))
               },
               shape = CircleShape,
               containerColor = MaterialTheme.colorScheme.primary,
               contentColor = MaterialTheme.colorScheme.onPrimary
           ){
               Icon(
                   modifier = Modifier.size(32.dp),
                   imageVector = Icons.Filled.Add,
                   contentDescription = "Add icon",
                   tint = MaterialTheme.colorScheme.onPrimary
               )
           }

       }
   ) {

       LazyColumn(
           modifier = Modifier
               .fillMaxSize()
               .padding(top = 8.dp),
           verticalArrangement = Arrangement.spacedBy(8.dp)
       ) {
           items(categories){category->
              CategoryItem(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(horizontal = 8.dp),
                      category = category,
                     onEvent = onEvent
              )
           }
       }

       AddCategory(
           showBottomSheet = showBottomSheet,
           onEvent = onEvent,
           colors = colors,
           selectedProductColor = selectedProductColor,
           categoryName = categoryName,
           categoryError = categoryError,
           category = category
       )
   }
}


@Composable
fun CategoryItem(
    modifier: Modifier,
    category: CategoryWithColor,
    onEvent: (InventoryEvent) -> Unit
){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 1.dp
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onEvent(InventoryEvent.SelectCategory(category = category)) }
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(category.color.color, CircleShape),
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    text = category.name,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }


            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onEvent(InventoryEvent.DeleteCategory(category = category)) },
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Icon",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }

}



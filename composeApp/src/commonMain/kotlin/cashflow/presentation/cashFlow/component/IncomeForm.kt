package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cashflow.presentation.cashFlow.CashFlowEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_new
import inventoryapp.composeapp.generated.resources.selectCategory
import org.jetbrains.compose.resources.stringResource
import settings.domain.model.category.CategoryWithColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeForm(
    modifier: Modifier,
    categories:List<CategoryWithColor>,
    onEvent: (CashFlowEvent) -> Unit,
    selectedCategory:CategoryWithColor?,
    showIncomeForm:Boolean,
    showCategoryDropDown:Boolean
){
 if(showIncomeForm){
     ModalBottomSheet(
         modifier = modifier,
         onDismissRequest = {
             onEvent(CashFlowEvent.ShowIncomeForm(false))
         }
     ){
         Column(
             modifier = Modifier.fillMaxWidth()
         ) {

             Box(
                 modifier = Modifier
                     .fillMaxWidth()
                     .background(
                         color = selectedCategory?.color?.color ?: MaterialTheme.colorScheme.onPrimary,
                         shape = RoundedCornerShape(8.dp)
                     )

             ){
                 Row(
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(8.dp)
                         .clickable {
                             onEvent(CashFlowEvent.ShowCategoryDropDown(true))
                         },
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Text(
                         modifier = Modifier.weight(1f),
                         text =selectedCategory?.name ?: stringResource(Res.string.selectCategory),
                         style = MaterialTheme.typography.titleMedium,
                         textAlign = TextAlign.Center,
                         color = if(selectedCategory == null) Color.Black else Color.White
                     )
                     Icon(
                         modifier = Modifier.size(32.dp),
                         imageVector =  Icons.Filled.ArrowDropDown  ,
                         contentDescription = "Dropdown arrow",
                         tint = if(selectedCategory == null) Color.Black else Color.White
                     )
                 }
                 DropdownMenu(
                     modifier = Modifier.fillMaxWidth(),
                     expanded = showCategoryDropDown,
                     onDismissRequest = {
                         onEvent(CashFlowEvent.ShowCategoryDropDown(false))
                     },
                 ){
                     categories.onEach { category ->
                         DropdownMenuItem(
                             text = {
                                 CategoryDropDownRow(
                                     backgroundColor = category.color.color,
                                     categoryName = category.name
                                 )
                             },
                             onClick = {
                                 onEvent(CashFlowEvent.SelectedCategory(category))
                             }
                         )
                     }
                     DropdownMenuItem(
                         text = {

                             CategoryDropDownRow(
                                 backgroundColor = Color.Transparent,
                                 categoryName = stringResource(Res.string.add_new)
                             )
                         },
                         onClick = {

                         }
                     )


                 }

             }

             Box(modifier = Modifier
                 .fillMaxWidth()
                 .height(200.dp))

         }

     }
 }

}


@Composable
fun CategoryDropDownRow(
    backgroundColor: Color,
    categoryName:String
){
    Row {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(0.dp)
                )
        )
        Text(
            modifier = Modifier.weight(1f),
            text = categoryName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall
        )
    }
}
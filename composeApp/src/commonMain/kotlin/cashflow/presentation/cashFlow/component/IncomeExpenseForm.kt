package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cashflow.domain.enums.IncomeExpenseType
import cashflow.presentation.cashFlow.CashFlowEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_expense
import inventoryapp.composeapp.generated.resources.add_income
import inventoryapp.composeapp.generated.resources.add_new
import inventoryapp.composeapp.generated.resources.enter_amount
import inventoryapp.composeapp.generated.resources.money_hint
import inventoryapp.composeapp.generated.resources.note
import inventoryapp.composeapp.generated.resources.selectCategory
import org.jetbrains.compose.resources.stringResource
import settings.domain.model.category.CategoryWithColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeExpenseForm(
    modifier: Modifier,
    categories:List<CategoryWithColor>,
    onEvent: (CashFlowEvent) -> Unit,
    selectedCategory:CategoryWithColor?,
    showIncomeForm:Boolean,
    showCategoryDropDown:Boolean,
    bottomSheetForAddCategory:()->Unit,
    amount:String?,
    note:String?,
    today:String,
    incomeExpenseType: IncomeExpenseType
){

 if(showIncomeForm){
     ModalBottomSheet(
         modifier = modifier,
         dragHandle = null,
         onDismissRequest = {
             onEvent(CashFlowEvent.ShowIncomeExpenseForm(false,incomeExpenseType))
         }
     ){
         Column(
             modifier = Modifier.fillMaxWidth(),
             verticalArrangement = Arrangement.spacedBy(16.dp)
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
                             bottomSheetForAddCategory()
                         }
                     )


                 }

             }


            FormCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(horizontal =  16.dp),
                value = amount ?: "",
                onValue = {
                    onEvent(CashFlowEvent.EnterAmount(it))
                },
                keyboardType = KeyboardType.Number,
                hint = stringResource(Res.string.money_hint,"TSH"),
                label = stringResource(Res.string.enter_amount),
                textAlign = TextAlign.Center,
                titleStyle = MaterialTheme.typography.titleLarge,
                bodyStyle = MaterialTheme.typography.titleLarge
            )

             FormCard(
                 modifier = Modifier
                     .fillMaxWidth()
                     .background(color = Color.White)
                     .padding(horizontal =  16.dp),
                 value = note ?: "",
                 onValue = {
                     onEvent(CashFlowEvent.EnterNote(it))
                 },
                 keyboardType = KeyboardType.Text,
                 hint = null,
                 label = stringResource(Res.string.note),
                 textAlign = TextAlign.Start,
                 titleStyle = MaterialTheme.typography.bodyMedium.copy(
                     color = MaterialTheme.colorScheme.tertiary,
                     fontWeight = FontWeight.Normal,
                 ),
                 bodyStyle = MaterialTheme.typography.bodyMedium
             )

             Text(
                 modifier = Modifier.fillMaxWidth(),
                 text = today,
                 style = MaterialTheme.typography.titleLarge,
                 textAlign = TextAlign.Center
             )
             Button(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(horizontal = 16.dp)
                     .padding(bottom = 16.dp),
                 shape = RoundedCornerShape(8.dp),
                 colors = ButtonDefaults.buttonColors(
                     containerColor = if(incomeExpenseType == IncomeExpenseType.INCOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                 ),
                 onClick = {
                     onEvent(CashFlowEvent.SaveIncomeOrExpense)
                 }
             ){
                 Text(
                     text = stringResource(if(incomeExpenseType == IncomeExpenseType.INCOME)Res.string.add_income else Res.string.add_expense).uppercase()
                 )
             }

         }

     }
 }

}


@Composable
fun FormCard(
    modifier: Modifier,
    value:String,
    onValue: (String) -> Unit,
    keyboardType: KeyboardType,
    hint: String?,
    label:String,
    textAlign: TextAlign,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    bodyStyle: TextStyle = MaterialTheme.typography.titleLarge
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = label,
                textAlign = TextAlign.Center,
                style = titleStyle,
                fontWeight = FontWeight.Bold
            )
            //stringResource(Res.string.money_hint,"TSH"
            CashFlowTextField(
                modifier = Modifier.fillMaxWidth(),
                hint = hint,
                value = value,
                onValue =onValue,
                keyboardType = keyboardType,
                textAlign = textAlign,
                style = bodyStyle
            )

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

@Composable
fun CashFlowTextField(
    modifier: Modifier,
    hint:String? = null,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    value:String,
    onValue:(String) -> Unit,
    keyboardType: KeyboardType  = KeyboardType.Text,
    textAlign: TextAlign = TextAlign.Center
){
    TextField(
        modifier = modifier,
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
        value = value,
        onValueChange = onValue,
        placeholder = {
            if(hint != null){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = hint,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Gray.copy(alpha = 0.7f),
                    style = style,
                    textAlign = TextAlign.Center
                )
            }
        },
        textStyle = style.copy(
            textAlign = textAlign,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),

    )
}
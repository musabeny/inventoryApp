package cashflow.presentation.purchaseReceipt

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cashflow.data.mapper.toDateMonthYearFormat
import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.BillItem
import core.util.UiEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.edit
import inventoryapp.composeapp.generated.resources.itemName
import inventoryapp.composeapp.generated.resources.price
import inventoryapp.composeapp.generated.resources.purchase_receipt
import inventoryapp.composeapp.generated.resources.sno
import inventoryapp.composeapp.generated.resources.total
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseReceiptScreen(
    state: PurchaseReceiptState,
    onEvent: (PurchaseReceiptEvent) -> Unit,
    snackBarHost: SnackbarHostState,
    uiEvent: Flow<UiEvent>,
    navController: NavController,
     billId:Long?
){
    LaunchedEffect(true){
        onEvent(PurchaseReceiptEvent.BillWithItems(billId))
        uiEvent.collect{event ->
            when(event){
                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
                is UiEvent.PopBackStack -> {
                    navController.navigateUp()
                }
                is UiEvent.ShowSnackBar -> {
                    snackBarHost.showSnackbar(event.message.asString())
                }
            }
        }
    }

   Scaffold(
       modifier = Modifier.fillMaxSize(),
       topBar = {
           CenterAlignedTopAppBar(
               title = {
                   Text(
                       text = stringResource(Res.string.purchase_receipt),
                       style = MaterialTheme.typography.titleMedium.copy(
                           color = MaterialTheme.colorScheme.onPrimary,
                           fontWeight = FontWeight.SemiBold
                       )
                   )
               },
               navigationIcon = {
                   CustomIcon(
                       modifier = Modifier.clickable {
                           onEvent(PurchaseReceiptEvent.NavigateBack)
                       },
                       imageVector = Icons.Filled.Close,
                   )
               },
               actions = {
                   CustomIcon(
                       modifier = Modifier.clickable {
                           onEvent(PurchaseReceiptEvent.NavigateToAddPurchase(billId))
                       },
                       imageVector = Icons.Filled.Edit,
                   )
                   Spacer(modifier = Modifier.width(8.dp))
                   CustomIcon(
                       imageVector = Icons.Filled.Share,
                   )
               },
               colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                   containerColor = MaterialTheme.colorScheme.primary
               )
           )
       }
   ) {
       Column(
           modifier = Modifier
               .fillMaxSize()
               .padding(top = it.calculateTopPadding())
               .padding(horizontal = 8.dp),
           verticalArrangement = Arrangement.spacedBy(10.dp)
       ) {
           PurchaseBill(
               modifier = Modifier.fillMaxWidth(),
               bill = state.billItems?.bill
           )
           PurchaseItems(
               modifier = Modifier.fillMaxWidth(),
               items = state.billItems?.items ?: emptyList()
           )
           Button(
               modifier = Modifier.fillMaxWidth(),
               onClick = {
                   onEvent(PurchaseReceiptEvent.NavigateToAddPurchase(billId))
               },
               colors = ButtonDefaults.buttonColors().copy(
                   containerColor = MaterialTheme.colorScheme.secondary
               )
               ,
               shape = RoundedCornerShape(4.dp)
           ){
               Text(
                   text = stringResource(Res.string.edit),
                   color = MaterialTheme.colorScheme.onSecondary
               )
           }
       }

   }

}

@Composable
fun PurchaseBill(
    modifier: Modifier,
    bill: Bill?
){

    Surface(
      modifier = modifier,
      shadowElevation = 2.dp ,
      shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
               ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment =Alignment.CenterHorizontally
        ) {
            Text(
                text = "MB-${bill?.id ?: 0}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = bill?.billTitle ?: "",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = bill?.dateCreated?.toDateMonthYearFormat() ?: "",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun PurchaseItems(
    modifier: Modifier,
    items: List<BillItem>
){
    val localDensity = LocalDensity.current
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    Surface(
        modifier = modifier,
        shadowElevation = 2.dp ,
        shape = RoundedCornerShape(4.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .onGloballyPositioned {
                            itemHeight = with(localDensity){it.size.height.toDp()}
                        }
                ) {
                    PurchaseText(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                            ,
                        label = stringResource(Res.string.sno),
                        textAlign = TextAlign.Center
                    )
                    VerticalDivider(
                        modifier = Modifier.height(itemHeight),
                        color = DividerDefaults.color.copy(alpha = 0.5f)
                    )
                    PurchaseText(
                        modifier = Modifier
                            .weight(3f)
                            .padding(start = 4.dp)
                            .padding(vertical = 8.dp),
                        label = stringResource(Res.string.itemName)
                    )
                    VerticalDivider(
                        modifier = Modifier.height(itemHeight),
                        color = DividerDefaults.color.copy(alpha = 0.5f)
                    )
                    PurchaseText(
                        modifier = Modifier
                            .weight(2f)
                            .padding(vertical = 8.dp),
                        label = stringResource(Res.string.price),
                        textAlign = TextAlign.End
                    )
                }
                HorizontalDivider(
                    color = DividerDefaults.color.copy(alpha = 0.5f)
                )
            }
            itemsIndexed(items){index,item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    PurchaseText(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                        label = "${index+1}",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    VerticalDivider(
                        modifier = Modifier.height(itemHeight),
                        color = DividerDefaults.color.copy(alpha = 0.5f)
                    )
                    PurchaseText(
                        modifier = Modifier
                            .weight(3f)
                            .padding(start = 4.dp)
                            .padding(vertical = 8.dp),
                        label = item.itemName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    VerticalDivider(
                        modifier = Modifier.height(itemHeight),
                        color = DividerDefaults.color.copy(alpha = 0.5f)
                    )
                    PurchaseText(
                        modifier = Modifier
                            .weight(2f)
                            .padding(vertical = 8.dp),
                        label = "${item.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.End
                    )
                }
                HorizontalDivider(
                    color = DividerDefaults.color.copy(alpha = 0.5f)
                )
            }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    PurchaseText(
                        modifier = Modifier.weight(4f),
                        label = stringResource(Res.string.total),
                        textAlign = TextAlign.End
                    )
                    PurchaseText(
                        modifier = Modifier
                            .weight(2f)
                            ,
                        label = "${items.map { it.price }.sumOf { it }}",
                        textAlign = TextAlign.End
                    )
                }
            }
        }

    }
}
@Composable
fun PurchaseText(
    modifier: Modifier,
    label:String,
    textAlign: TextAlign? = null,
    style:TextStyle = MaterialTheme.typography.bodyLarge.copy(
        fontWeight = FontWeight.Bold
    )
){
    Text(
        modifier = modifier,
        text = label,
        style = style,
        textAlign = textAlign
    )
}

@Composable
fun CustomIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    color:Color = MaterialTheme.colorScheme.onPrimary
){
    Icon(
        modifier = modifier,
        imageVector = imageVector,
        contentDescription = "Icon",
        tint = color
    )
}
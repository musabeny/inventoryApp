package cashflow.presentation.cashFlow.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.navigation.NavController
import cashflow.data.mapper.daysOfDate
import cashflow.domain.enums.CashFlowTabs
import cashflow.domain.enums.DaysOfDate
import cashflow.domain.enums.ListViewType
import cashflow.domain.model.purchase.BillAndItems
import cashflow.domain.model.purchase.ItemColor
import cashflow.domain.model.purchase.PurchaseDetail
import cashflow.domain.model.purchase.PurchaseGroupByItem
import cashflow.presentation.cashFlow.CashFlowEvent
import core.extensions.addZeroBefore
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_purchase
import inventoryapp.composeapp.generated.resources.draft
import inventoryapp.composeapp.generated.resources.item_count
import inventoryapp.composeapp.generated.resources.today
import inventoryapp.composeapp.generated.resources.yesterday
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

@Composable
fun PurchasePage(
    onEvent: (CashFlowEvent) -> Unit,
    navController: NavController,
    purchasedByDate:Map<LocalDate, List<PurchaseDetail>>,
    purchasedByItem: Map<String, List<PurchaseGroupByItem>>,
    viewType: ListViewType,
    totalPurchase:String
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        PurchasePageList(
            modifier = Modifier.fillMaxSize(),
            onEvent = onEvent,
            purchasedByDate = purchasedByDate,
            purchasedByItem = purchasedByItem,
            listViewType = viewType
        ){
            FilterAndTotalHeader(
                modifier = Modifier.fillMaxWidth(),
                viewType = viewType,
                onEvent = onEvent,
                expense = "",
                income = "",
                tab = CashFlowTabs.PURCHASE,
                totalPurchase = totalPurchase
            )
        }


       Button(
           modifier = Modifier.padding(10.dp),
           onClick = {
              onEvent(CashFlowEvent.GoToPurchase(navController))
           }
       ){
           Text(
               text = stringResource(Res.string.add_purchase),
               fontWeight = FontWeight.Bold
           )
       }
    }
}


@Composable
fun PurchasePageList(
    modifier: Modifier,
    onEvent: (CashFlowEvent) -> Unit,
    purchasedByDate:Map<LocalDate, List<PurchaseDetail>>,
    purchasedByItem: Map<String, List<PurchaseGroupByItem>>,
    listViewType: ListViewType,
    header:@Composable () -> Unit

){
    if(listViewType == ListViewType.LIST){
      PurchasedViewByList(
          modifier = modifier,
          purchasedByDate = purchasedByDate,
          onEvent = onEvent,
          header = header
      )
    }else{
        PurchasedViewByGroup(
            modifier = modifier,
            purchasedByItem = purchasedByItem,
            header = header
        )
    }

}

@Composable
fun PurchasedViewByList(
    modifier: Modifier,
    purchasedByDate:Map<LocalDate, List<PurchaseDetail>>,
    onEvent: (CashFlowEvent) -> Unit,
    header:@Composable () -> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            if( purchasedByDate.isNotEmpty() ){
                header()
            }
        }
        purchasedByDate.forEach { (localDate,purchased) ->
            item {
                HeaderPurchase(
                    day = purchased.first().day,
                    dayInWord = purchased.first().dayName?.let { stringResource(it) },
                    monthYear = purchased.first().monthYear,
                    purchaseTotal = "${purchased.sumOf { it.total }}"
                )
            }
            items(purchased.reversed()){detail ->
                ItemPurchase(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(CashFlowEvent.NavigateToPurchaseReceipt(detail.billId))
                        },
                    label = "MB-${detail.billId} | ${detail.billName}",
                    count = detail.itemCount,
                    note = detail.note ?: "",
                    amount = "${detail.total}",
                    isDraft = detail.isDaft
                )
            }
        }
    }
}

@Composable
fun PurchasedViewByGroup(
    modifier: Modifier,
    purchasedByItem:Map<String, List<PurchaseGroupByItem>>,
    header:@Composable () -> Unit
){
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        item(
            span = {
                GridItemSpan(3)
            }
        ){
            if( purchasedByItem.isNotEmpty() ){
                header()
            }
        }

        items(purchasedByItem.keys.toList()){item ->

            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(8.dp),
                color = purchasedByItem[item]?.first()?.color ?: Color.Magenta,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "${purchasedByItem[item]?.sumOf { it.price } ?: 0.0}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

    }
}


@Composable
fun HeaderPurchase(
    day:String,
    dayInWord:String?,
    monthYear:String,
    purchaseTotal:String
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text =day.addZeroBefore(),
                style = MaterialTheme.typography.titleMedium
            )
            Column {
                dayInWord?.let {word ->
                    Text(
                        text = word,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Text(
                    text = monthYear,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

        Text(
            modifier = Modifier.weight(1f),
            text = "TSH$purchaseTotal",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.End
        )
    }

}

@Composable
fun ItemPurchase(
    modifier: Modifier,
    label:String,
    count:String,
    note:String,
    amount:String,
    isDraft:Boolean
){
    Surface(
        modifier  = modifier,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 1.dp
    ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp),
           horizontalArrangement = Arrangement.SpaceBetween
       ) {
           Column {
               Text(
                   text = label,
                   style = MaterialTheme.typography.titleSmall.copy(
                       fontWeight = FontWeight.SemiBold
                   )
               )
               Text(
                   text = stringResource(Res.string.item_count,count),
                   style = MaterialTheme.typography.labelSmall.copy(
                       color = MaterialTheme.colorScheme.outline
                   )
               )
               Text(
                   text = note,
                   style = MaterialTheme.typography.labelSmall.copy(
                       color = MaterialTheme.colorScheme.outline
                   )
               )
           }
           Column {
               Text(
                   text = "TSH$amount",
                   style = MaterialTheme.typography.titleSmall.copy(
                       color = MaterialTheme.colorScheme.error
                   )
               )
               if(isDraft){
                   Text(
                       text = stringResource(Res.string.draft),
                       style = MaterialTheme.typography.titleSmall.copy(
                           color = MaterialTheme.colorScheme.onPrimaryContainer
                       )
                   )
               }

           }
       }
    }
}


@Composable
fun getRandomMaterialColor(): ItemColor {
    val colorScheme = MaterialTheme.colorScheme
    val colors = listOf(
        ItemColor(colorScheme.primary , colorScheme.onPrimary),
        ItemColor(colorScheme.secondary , colorScheme.onSecondary),
        ItemColor(colorScheme.tertiary , colorScheme.onTertiary),
        ItemColor(colorScheme.background , colorScheme.onBackground),
        ItemColor(colorScheme.surface , colorScheme.onSurface),
        ItemColor(colorScheme.error , colorScheme.onError),
        ItemColor(colorScheme.primaryContainer , colorScheme.onPrimaryContainer),
        ItemColor(colorScheme.secondaryContainer , colorScheme.onSecondaryContainer),
        ItemColor(colorScheme.tertiaryContainer , colorScheme.onTertiaryContainer),
        ItemColor(colorScheme.surface , colorScheme.onSurface)
    )
    return colors.random()
}


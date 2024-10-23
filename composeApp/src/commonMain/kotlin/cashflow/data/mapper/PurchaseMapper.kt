package cashflow.data.mapper

import cashflow.domain.enums.DaysOfDate
import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.BillAndItems
import cashflow.domain.model.purchase.BillItem
import cashflow.domain.model.purchase.PurchaseDetail
import cashflow.domain.model.purchase.PurchaseGroupByItem
import cashflow.domain.model.purchase.PurchaseItem
import cashflow.domain.usecase.PurchaseUseCase
import cashflow.domain.usecase.cashFlowCases.RandomColor
import database.entity.BillEntity
import database.entity.BillItemEntity
import database.entity.relation.BillWithItems
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.today
import inventoryapp.composeapp.generated.resources.yesterday
import org.jetbrains.compose.resources.stringResource

fun Bill.toBillEntity():BillEntity{
    return BillEntity(
        id = id,
        billTitle = billTitle,
        note = note,
        isDraft = isDraft,
        dateCreated = dateCreated
    )
}

fun  BillEntity.toBill():Bill{
    return Bill(
        id = id,
        billTitle = billTitle,
        note = note,
        isDraft = isDraft,
        dateCreated = dateCreated
    )
}

fun BillItem.toBillItemEntity():BillItemEntity{
    return BillItemEntity(
        id = id,
        itemName = itemName,
        price = price,
        billId = billId
    )
}

fun BillItemEntity.toBillItem():BillItem{
    return BillItem(
        id = id,
        itemName = itemName,
        price = price,
        billId = billId
    )
}

fun BillWithItems.toBillAndItems():BillAndItems{
    return BillAndItems(
        bill = bill.toBill(),
        items = items.map { it.toBillItem() }
    )
}

fun BillAndItems.toPurchaseDetail(): PurchaseDetail {
    return PurchaseDetail(
        day = "${bill.dateCreated.dayOfMonth}",
        dayName = when(bill.dateCreated.daysOfDate()){
            DaysOfDate.TODAY -> Res.string.today
            DaysOfDate.YESTERDAY -> Res.string.yesterday
            else -> null
        },
        monthYear = bill.dateCreated.formatToMonthYear(),
        total = items.sumOf { it.price },
        itemCount ="${items.size}",
        note = bill.note,
        isDaft = bill.isDraft,
        billName = bill.billTitle,
        billId = if(bill.id == null) null else "${bill.id}",
        dateCreated = bill.dateCreated
    )
}

fun BillItem.toPurchaseGroupByItem(useCase: PurchaseUseCase): PurchaseGroupByItem {
    return PurchaseGroupByItem(
        name = itemName,
        price = price,
        color = useCase.randomColor()
    )
}

fun PurchaseItem.toBillItem():BillItem{
    return BillItem(
        id = id,
        itemName = name,
        price = price.toDoubleOrNull() ?: 0.0 ,
        billId = billId
    )
}
fun  BillItem.toBillItem():PurchaseItem{
    return PurchaseItem(
        id = id,
        name = itemName,
        price = price.toString() ,
        billId = billId
    )
}
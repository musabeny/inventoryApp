package cashflow.data.mapper

import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.BillItem
import database.entity.BillEntity
import database.entity.BillItemEntity

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
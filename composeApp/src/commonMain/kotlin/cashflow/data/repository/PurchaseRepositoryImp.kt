package cashflow.data.repository

import cashflow.data.mapper.toBill
import cashflow.data.mapper.toBillAndItems
import cashflow.data.mapper.toBillEntity
import cashflow.data.mapper.toBillItem
import cashflow.data.mapper.toBillItemEntity
import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.BillItem
import cashflow.domain.model.purchase.BillAndItems
import cashflow.domain.repository.PurchaseRepository
import database.InventoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class PurchaseRepositoryImp(
    private val db: InventoryDatabase,
): PurchaseRepository {
    override suspend fun savePurchase(bill: Bill,billItems: List<BillItem>):Int {
       return db.billDao().insertBillWithItems(bill.toBillEntity(),billItems.map { it.toBillItemEntity() })
    }

    override fun purchases(startDate: LocalDate, endDate: LocalDate): Flow<List<BillAndItems>> {
      return  db.billDao().billWithItems(startDate,endDate).map {bills -> bills.map { it.toBillAndItems() }}
    }

    override fun purchasesFiltered(
        startDate: LocalDate,
        endDate: LocalDate,
        isDraft: List<Boolean>
    ): Flow<List<BillAndItems>> {
        return db.billDao().billWithItemsFiltered(startDate, endDate, isDraft).map {bills -> bills.map { it.toBillAndItems() }}
    }

    override suspend fun searchedByItemName(
        startDate: LocalDate,
        endDate: LocalDate,
        itemName: String
    ): Flow<List<BillAndItems>> {
        return db.billDao().searchedByItemName(startDate, endDate, itemName).map {bills -> bills.map { it.toBillAndItems() }}
    }

    override suspend fun searchedByBillOrNote(
        startDate: LocalDate,
        endDate: LocalDate,
        searchText: String
    ): Flow<List<BillAndItems>> {
        return db.billDao().searchBill(startDate,endDate,searchText).map {bills -> bills.map { it.toBillAndItems() }}
    }

    override fun findPurchases(billId: Long): Flow<BillAndItems> {
       return db.billDao().findBillWithItems(billId).map {it.toBillAndItems()}
    }

    override suspend fun updateBillWithItems(bill: Bill, billItems: List<BillItem>): Int {
       return db.billDao().updateBillWithItems(bill.toBillEntity(),billItems.map { it.toBillItemEntity() })
    }

    override suspend fun deleteItem(item: BillItem) {
        db.billDao().deleteItem(item.toBillItemEntity())
    }
}
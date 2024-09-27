package cashflow.data.repository

import cashflow.data.mapper.toBillEntity
import cashflow.data.mapper.toBillItemEntity
import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.BillItem
import cashflow.domain.repository.PurchaseRepository
import database.InventoryDatabase

class PurchaseRepositoryImp(
    private val db: InventoryDatabase,
): PurchaseRepository {
    override suspend fun savePurchase(bill: Bill,billItems: List<BillItem>) {
        db.billDao().insertBillWithItems(bill.toBillEntity(),billItems.map { it.toBillItemEntity() })
    }
}
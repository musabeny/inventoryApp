package cashflow.domain.repository

import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.BillItem

interface PurchaseRepository {
    suspend fun savePurchase(bill: Bill, billItems: List<BillItem>)
}
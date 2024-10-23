package cashflow.domain.repository

import cashflow.domain.model.purchase.Bill
import cashflow.domain.model.purchase.BillItem
import cashflow.domain.model.purchase.BillAndItems
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface PurchaseRepository {
    suspend fun savePurchase(bill: Bill, billItems: List<BillItem>):Int
    fun purchases(startDate: LocalDate,endDate: LocalDate):Flow<List<BillAndItems>>
    fun purchasesFiltered(startDate: LocalDate,endDate: LocalDate,isDraft:List<Boolean>):Flow<List<BillAndItems>>

    suspend fun searchedByItemName(startDate: LocalDate,endDate: LocalDate,itemName:String):Flow<List<BillAndItems>>

    suspend fun searchedByBillOrNote(startDate: LocalDate,endDate: LocalDate,searchText:String):Flow<List<BillAndItems>>
    fun findPurchases(billId:Long):Flow<BillAndItems>

    suspend fun updateBillWithItems(bill: Bill, billItems: List<BillItem>):Int
    suspend fun deleteItem(item: BillItem)

}
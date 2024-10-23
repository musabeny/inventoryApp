package database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import cashflow.domain.model.purchase.BillAndItemIds
import database.entity.BillEntity
import database.entity.BillItemEntity
import database.entity.CategoryEntity
import database.entity.relation.BillWithItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate

@Dao
interface BillDao {
    @Insert
    suspend fun insertBill(bill: BillEntity):Long

    @Insert
    suspend fun insertBillItems(items: List<BillItemEntity>):List<Long>

    @Delete
    suspend fun deleteItem(item:BillItemEntity)

    @Transaction
    suspend fun insertBillWithItems(bill: BillEntity,items: List<BillItemEntity>):Int{
        val billId = insertBill(bill)
        val billItems = items.map { it.copy(billId = billId) }
        val itemsIds =  insertBillItems(billItems)
        return itemsIds.size
    }

    @Update
    suspend fun updateBill(bill: BillEntity)

    @Update
    suspend fun updateBillItem(items: List<BillItemEntity>):Int

    @Transaction
    suspend fun updateBillWithItems(bill: BillEntity,items: List<BillItemEntity>):Int{
       updateBill(bill)
        val newItems = items.filter { it.id == null }
        insertBillItems(newItems)
      return updateBillItem(items.filter { it.id != null })
    }

    @Transaction
    @Query("SELECT * FROM BillEntity WHERE dateCreated BETWEEN :startDate AND :endDate ORDER BY dateCreated DESC ")
    fun billWithItems(startDate: LocalDate, endDate: LocalDate):Flow<List<BillWithItems>>

    @Transaction
    @Query("SELECT * FROM BillEntity WHERE id =:billId ")
    fun findBillWithItems(billId:Long):Flow<BillWithItems>

    @Transaction
    @Query("SELECT * FROM BillEntity WHERE dateCreated BETWEEN :startDate AND :endDate AND isDraft IN (:isDraft) ORDER BY dateCreated DESC ")
    fun billWithItemsFiltered(startDate: LocalDate, endDate: LocalDate,isDraft:List<Boolean>):Flow<List<BillWithItems>>

    @Transaction
    @Query("""
        SELECT id,bill_id FROM BillItemEntity
        JOIN BillItemFts ON BillItemFts.itemName == BillItemEntity.itemName
        WHERE BillItemFts.itemName MATCH :name
    """)
    fun searchBillItemByName(name:String):Flow<List<BillAndItemIds>>


    @Transaction
    @Query("SELECT * FROM BillEntity WHERE dateCreated BETWEEN :startDate AND :endDate AND id IN (:bills) ORDER BY dateCreated DESC ")
    fun billWithItemsSearched(startDate: LocalDate, endDate: LocalDate,bills:List<Long>):Flow<List<BillWithItems>>

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun  searchedByItemName(startDate: LocalDate, endDate: LocalDate,itemName:String):Flow<List<BillWithItems>> {
       val billIds = searchBillItemByName(itemName)
       val billWithItem = billIds.map { bills ->
           val uniqueBills = bills.map { it.bill_id }.distinct()
           billWithItemsSearched(startDate, endDate, uniqueBills).map { items -> items.map {item -> item.copy(items = item.items.filter {it.id in bills.map { it.id }  }) } }
        }
       return billWithItem.flattenConcat()
    }


    @Transaction
    @Query("""
        SELECT * FROM BillEntity
        JOIN BillFts ON BillFts.billTitle == BillEntity.billTitle AND BillFts.note == BillEntity.note
        WHERE dateCreated BETWEEN :startDate AND :endDate AND BillFts.billTitle MATCH :searchText OR BillFts.note MATCH :searchText
    """)
    fun searchBill(startDate: LocalDate,
                   endDate: LocalDate,
                   searchText:String
    ):Flow<List<BillWithItems>>

}
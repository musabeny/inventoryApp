package database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import database.entity.BillEntity
import database.entity.BillItemEntity
import database.entity.relation.BillWithItems
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface BillDao {
    @Insert
    suspend fun insertBillWithItems(bill: BillEntity,items: List<BillItemEntity>)

    @Transaction
    @Query("SELECT * FROM BillEntity WHERE dateCreated BETWEEN :startDate AND :endDate")
    fun billWithItems(startDate: LocalDate, endDate: LocalDate):Flow<List<BillWithItems>>
}
package database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import database.entity.BillEntity
import database.entity.BillItemEntity

data class BillWithItems(
    @Embedded val bill:BillEntity,
   @Relation(
       parentColumn = "id",
       entityColumn = "bill_id"
   )
   val items:List<BillItemEntity>
)

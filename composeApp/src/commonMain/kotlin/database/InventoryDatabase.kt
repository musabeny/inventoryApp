
package database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import database.conventer.LocalDateConverter
import database.dao.BillDao
import database.dao.CategoryDao
import database.dao.IncomeExpenseDao
import database.dao.ProductDao
import database.entity.BillEntity
import database.entity.BillItemEntity
import database.entity.CategoryEntity
import database.entity.CategoryFts
import database.entity.IncomeExpenseEntity
import database.entity.ProductEntity
import database.entity.ProductFts
import di.Factory

@Database(
    entities = [
        ProductEntity::class,
        ProductFts::class,
        CategoryEntity::class,
        CategoryFts::class,
        IncomeExpenseEntity::class,
        BillEntity::class,
        BillItemEntity::class
               ],
    version = 1
)
@TypeConverters(LocalDateConverter::class)
@ConstructedBy(InventoryDatabaseConstructor::class)
abstract class InventoryDatabase:RoomDatabase(),DB {
    abstract fun productDao():ProductDao
    abstract fun categoryDao():CategoryDao
    abstract fun incomeExpenseDao():IncomeExpenseDao
    abstract fun billDao():BillDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object InventoryDatabaseConstructor : RoomDatabaseConstructor<InventoryDatabase>


interface DB {
    fun clearAllTables(): Unit {}
}

internal const val dbFileName = "inventory.db"

// The Room compiler generates the `actual` implementations.

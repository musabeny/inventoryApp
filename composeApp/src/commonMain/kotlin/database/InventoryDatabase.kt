
package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import database.conventer.LocalDateConverter
import database.dao.CategoryDao
import database.dao.IncomeExpenseDao
import database.dao.ProductDao
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
        IncomeExpenseEntity::class
               ],
    version = 1
)
@TypeConverters(LocalDateConverter::class)
abstract class InventoryDatabase:RoomDatabase(),DB {
    abstract fun productDao():ProductDao
    abstract fun categoryDao():CategoryDao
    abstract fun incomeExpenseDao():IncomeExpenseDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

interface DB {
    fun clearAllTables(): Unit {}
}

internal const val dbFileName = "inventory.db"

// The Room compiler generates the `actual` implementations.

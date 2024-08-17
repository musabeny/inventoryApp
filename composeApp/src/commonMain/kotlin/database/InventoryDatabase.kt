package database

import androidx.room.Database
import androidx.room.RoomDatabase
import database.dao.CategoryDao
import database.dao.ProductDao
import database.entity.CategoryEntity
import database.entity.CategoryFts
import database.entity.ProductEntity
import database.entity.ProductFts

@Database(
    entities = [
        ProductEntity::class,
        ProductFts::class,
        CategoryEntity::class,
        CategoryFts::class
               ],
    version = 1
)
abstract class InventoryDatabase:RoomDatabase(),DB {
    abstract fun productDao():ProductDao
    abstract fun categoryDao():CategoryDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

interface DB {
    fun clearAllTables(): Unit {}
}

internal const val dbFileName = "inventory.db"
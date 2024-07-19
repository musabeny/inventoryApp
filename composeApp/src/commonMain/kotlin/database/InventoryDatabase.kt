package database

import androidx.room.Database
import androidx.room.RoomDatabase
import database.dao.ProductDao
import database.entity.ProductEntity
import database.entity.ProductFts

@Database(
    entities = [ProductEntity::class,ProductFts::class],
    version = 1
)
abstract class InventoryDatabase:RoomDatabase() {
    abstract fun productDao():ProductDao
}

internal const val dbFileName = "inventory.db"
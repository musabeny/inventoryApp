package database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun getInventoryDatabase():InventoryDatabase{
    val dbFilePath = NSHomeDirectory() + "/$dbFileName"
    return Room.databaseBuilder<InventoryDatabase>(
        name = dbFilePath,
        factory =  { InventoryDatabase::class.instantiateImpl() }
    ).setDriver(BundledSQLiteDriver())
        .build()
}
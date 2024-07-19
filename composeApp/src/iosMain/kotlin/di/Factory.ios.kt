package di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.InventoryDatabase
import database.dbFileName
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual class Factory {
    actual fun createRoomDatabase(): InventoryDatabase {
        val dbFile = "${fileDirectory()}/$dbFileName"
        return Room.databaseBuilder<InventoryDatabase>(
            name = dbFile,
            factory =  { InventoryDatabase::class.instantiateImpl() }
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun fileDirectory(): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory).path!!
    }
}
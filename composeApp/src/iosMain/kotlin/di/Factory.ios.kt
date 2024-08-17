package di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.InventoryDatabase
import database.dbFileName
import database.instantiateImpl
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class Factory {
    actual fun createRoomDatabase(): InventoryDatabase {
        //"${fileDirectory()}/$dbFileName"
        val dbFile = NSHomeDirectory() + dbFileName
        return Room.databaseBuilder<InventoryDatabase>(
            name = dbFile,
            factory =  { InventoryDatabase::class.instantiateImpl() }
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

//    @OptIn(ExperimentalForeignApi::class)
//    private fun fileDirectory(): String {
//        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
//            directory = NSDocumentDirectory,
//            inDomain = NSUserDomainMask,
//            appropriateForURL = null,
//            create = false,
//            error = null,
//        )
//        return requireNotNull(documentDirectory).path!!
//    }
}
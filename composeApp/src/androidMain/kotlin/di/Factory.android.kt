package di

import android.app.Application
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.InventoryDatabase
import database.dbFileName
import kotlinx.coroutines.Dispatchers

actual class Factory(
    private val app:Application
) {
    actual fun createRoomDatabase(): InventoryDatabase {
        val dbFile = app.getDatabasePath(dbFileName)
        return Room.databaseBuilder<InventoryDatabase>(app,dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
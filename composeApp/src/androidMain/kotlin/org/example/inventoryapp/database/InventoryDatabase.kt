package org.example.inventoryapp.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.InventoryDatabase
import database.dbFileName

fun getInventoryDatabase(context:Context):InventoryDatabase{
   val dbFile = context.getDatabasePath(dbFileName)
    return Room.databaseBuilder<InventoryDatabase>(
        context = context.applicationContext,
        name = dbFile.name
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
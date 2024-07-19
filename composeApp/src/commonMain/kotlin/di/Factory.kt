package di

import database.InventoryDatabase

expect class Factory {
    fun createRoomDatabase():InventoryDatabase
}
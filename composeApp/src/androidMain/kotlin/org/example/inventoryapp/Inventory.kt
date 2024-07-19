package org.example.inventoryapp

import android.app.Application
import di.initKoin
import org.koin.android.ext.koin.androidContext

class Inventory:Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@Inventory)
//            androidLogger()
        }
    }
}
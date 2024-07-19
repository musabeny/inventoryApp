package di

import android.app.Application
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val dataBaseModule: Module = module {
//    Factory(app = Application()).createRoomDatabase()
//    singleOf(::Factory)

    single {
        Factory(get()).createRoomDatabase()
    }
//    single {Factory(get()).createRoomDatabase()}
}
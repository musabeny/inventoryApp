package di

import org.koin.core.module.Module
import org.koin.dsl.module

actual val dataBaseModule: Module = module {
    single {Factory().createRoomDatabase()  }
}
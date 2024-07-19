package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import settings.data.di.settingModule

fun initKoin(config:KoinAppDeclaration? = null){
    startKoin {
        config?.invoke(this)
        modules(appModule, dataBaseModule,settingModule)
    }
}
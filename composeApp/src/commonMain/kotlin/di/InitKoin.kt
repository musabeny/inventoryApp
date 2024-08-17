package di

import cashflow.data.di.cashFlowModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import settings.data.di.settingModule
import settings.data.di.settingRepositoryModule

fun initKoin(config:KoinAppDeclaration? = null){
    startKoin {
        config?.invoke(this)
        modules(appModule, dataBaseModule,settingModule,settingRepositoryModule, cashFlowModule)
    }
}
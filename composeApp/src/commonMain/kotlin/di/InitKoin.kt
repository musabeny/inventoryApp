package di

import cashflow.data.di.cashFlowModule
import cashflow.data.di.cashFlowRepositoryModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import sales.di.salesCaseModule
import settings.data.di.settingModule
import settings.data.di.settingRepositoryModule

fun initKoin(config:KoinAppDeclaration? = null){
    startKoin {
        config?.invoke(this)
        modules(appModule, dataBaseModule,settingModule,
            settingRepositoryModule, cashFlowModule,cashFlowRepositoryModule,salesCaseModule
        )
    }
}
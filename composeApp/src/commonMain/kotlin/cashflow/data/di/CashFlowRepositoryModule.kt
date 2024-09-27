package cashflow.data.di

import cashflow.data.repository.CashFlowRepositoryImp
import cashflow.data.repository.PurchaseRepositoryImp
import cashflow.domain.repository.CashFlowRepository
import cashflow.domain.repository.PurchaseRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val cashFlowRepositoryModule = module {
    singleOf(::CashFlowRepositoryImp).bind<CashFlowRepository>()
    singleOf(::PurchaseRepositoryImp).bind<PurchaseRepository>()
}
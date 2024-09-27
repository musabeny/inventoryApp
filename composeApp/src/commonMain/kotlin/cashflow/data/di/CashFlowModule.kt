package cashflow.data.di

import cashflow.domain.usecase.CashFlowUseCases
import cashflow.domain.usecase.PurchaseUseCase
import org.koin.dsl.module

val cashFlowModule = module {
    single { CashFlowUseCases() }
    single { PurchaseUseCase() }
}
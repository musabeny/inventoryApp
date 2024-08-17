package cashflow.data.di

import cashflow.domain.usecase.CashFlowUseCases
import org.koin.dsl.module

val cashFlowModule = module {
    single { CashFlowUseCases() }
}
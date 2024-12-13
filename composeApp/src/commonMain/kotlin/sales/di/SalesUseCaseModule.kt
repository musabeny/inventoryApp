package sales.di

import cashflow.domain.usecase.CashFlowUseCases
import cashflow.domain.usecase.PurchaseUseCase
import org.koin.dsl.module
import sales.domain.useCase.SaleUseCases
import sales.domain.useCase.useCases.CalculateItems

val salesCaseModule = module {
    single { SaleUseCases() }
    single { CalculateItems(get(),get()) }
}
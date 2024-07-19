package settings.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import settings.domain.useCase.ProductUseCase

val settingModule = module {
    single { ProductUseCase() }
}
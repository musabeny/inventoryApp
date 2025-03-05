package sales.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import sales.domain.repository.SalesRepository
import sales.data.repository.SalesRepositoryImp

val salesRepositoryModule = module {
    singleOf(::SalesRepositoryImp).bind<SalesRepository>()
}
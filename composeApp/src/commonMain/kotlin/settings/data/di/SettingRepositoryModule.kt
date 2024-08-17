package settings.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import settings.domain.repository.ProductRepository
import settings.data.repository.ProductRepositoryImp
import settings.domain.repository.CategoryRepository
import settings.data.repository.CategoryRepositoryImp

val settingRepositoryModule  = module{
    singleOf(::ProductRepositoryImp).bind<ProductRepository>()
    singleOf(::CategoryRepositoryImp).bind<CategoryRepository>()
}
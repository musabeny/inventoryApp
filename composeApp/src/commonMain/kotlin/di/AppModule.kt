package di

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import settings.data.repository.ProductRepositoryImp
import settings.domain.repository.ProductRepository
import settings.presentation.inventory.InventoryViewModel
import settings.presentation.product.ProductViewModel
import settings.presentation.setting.SettingViewModel

val appModule = module {
    single { "Hello word" }
    singleOf(::ProductRepositoryImp) .bind<ProductRepository>()
    viewModelOf(::SettingViewModel)
    viewModelOf(::InventoryViewModel)
    viewModelOf(::ProductViewModel)
}
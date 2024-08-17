package settings.data.repository

import database.InventoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import settings.data.mapper.toProduct
import settings.data.mapper.toProductEntity
import settings.domain.model.product.Product
import settings.domain.repository.ProductRepository
import settings.domain.useCase.ProductUseCase

class ProductRepositoryImp(
    private val db:InventoryDatabase,
    private val useCase: ProductUseCase
): ProductRepository {
    override suspend fun insertOrUpdateProduct(product: Product):Int {
         if(product.id == null){
          val insertResult =   db.productDao().insertProduct(product.toProductEntity())
             println("insertResult $insertResult")

             return if(insertResult > 0){
                 1
             }else{
                 0
             }
         }else {
           val updateResult =  db.productDao().updateProduct(product.toProductEntity())
             return if(updateResult > 0){
                 1
             }else{
                 0
             }
         }
//        return db.productDao().product(product.toProductEntity())
    }

    override suspend fun deleteProduct(product: Product) : Int{
        return db.productDao().deleteProduct(product.toProductEntity())
    }

    override  fun products(): Flow<List<Product>>  {
        return db.productDao().getAllProduct().map {products ->
            products.map {product ->
                product.toProduct(useCase.color())
            }
        }
    }

    override suspend fun findByItemCode(itemCode: String): String? {
        return db.productDao().findByItemCode(itemCode)
    }

    override suspend fun getLastItemCode(): String? {
        return db.productDao().getLastItemCode()
    }

    override suspend fun countProduct(): Int {
        return db.productDao().count()
    }

    override suspend fun getProductById(productId: Long): Product? {
        return db.productDao().getProductById(productId)?.toProduct(useCase.color())
    }

    override fun searchProduct(searchText:String): Flow<List<Product>> {
       return db.productDao().searchProductByName(searchText).map {products ->
           products.map { it.toProduct(useCase.color()) }
       }
    }

    override fun getExpiredProducts(): Flow<List<Product>> {
        return db.productDao().getExpiredProducts().map {products ->
            products.map {
                it.toProduct(useCase.color())
            }
        }
    }
}
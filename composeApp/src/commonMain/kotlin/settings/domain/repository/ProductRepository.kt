package settings.domain.repository

import kotlinx.coroutines.flow.Flow
import settings.domain.model.product.Product

interface ProductRepository {
    suspend fun insertOrUpdateProduct(product: Product):Int
    suspend fun deleteProduct(product: Product):Int
     fun products():Flow<List<Product>>
     suspend fun findByItemCode(itemCode:String):String?
     suspend fun getLastItemCode():String?
     suspend fun countProduct():Int
     suspend fun getProductById(productId:Long):Product?
    fun searchProduct(searchText:String):Flow<List<Product>>
    fun getExpiredProducts():Flow<List<Product>>
}
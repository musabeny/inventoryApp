package database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(product: ProductEntity):Long

    @Update
    suspend fun updateProduct(product: ProductEntity):Int

//    @Upsert
//    suspend fun product(product: ProductEntity):Int

    @Delete
    suspend fun deleteProduct(product: ProductEntity):Int

    @Query("SELECT * FROM ProductEntity")
     fun getAllProduct(): Flow<List<ProductEntity>>

    @Query("SELECT code FROM ProductEntity ORDER BY id DESC LIMIT 1")
    suspend fun getLastItemCode():String?

    @Query("SELECT code FROM ProductEntity WHERE code =:code")
    suspend fun findByItemCode(code:String):String?

    @Query("""
        SELECT * FROM ProductEntity
        JOIN ProductFts ON ProductFts.name == ProductEntity.name
        WHERE ProductFts.name MATCH :name
    """)
    fun searchProductByName(name:String):Flow<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE id = :productId")
    suspend fun getProductById(productId:Long):ProductEntity?

    @Query("SELECT * FROM ProductEntity WHERE  expireDate IS NOT NULL")
     fun getExpiredProducts():Flow<List<ProductEntity>>

    @Query("SELECT count(*) FROM ProductEntity")
    suspend fun count(): Int
}
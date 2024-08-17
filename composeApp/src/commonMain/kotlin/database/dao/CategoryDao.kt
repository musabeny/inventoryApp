package database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import database.entity.CategoryEntity
import database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: CategoryEntity):Long

    @Update
    suspend fun updateCategory(category: CategoryEntity):Int

    @Query("SELECT * FROM CategoryEntity ")
    fun getAllCategory():Flow<List<CategoryEntity>>

    @Delete
    suspend fun deleteCategory(category: CategoryEntity):Int

    @Query("""
        SELECT * FROM CategoryEntity
        JOIN CategoryFts ON CategoryFts.name == CategoryEntity.name
        WHERE CategoryFts.name MATCH :name
    """)
    fun searchCategoryByName(name:String):Flow<List<CategoryEntity>>
}
package settings.domain.repository

import kotlinx.coroutines.flow.Flow
import settings.domain.model.category.Category

interface CategoryRepository {
    suspend fun insertCategory(category: Category):Long
    suspend fun categories():Flow<List<Category>>
    suspend fun updateCategory(category: Category):Int
    suspend fun deleteCategory(category: Category):Int
    fun searchCategoryByName(name:String):Flow<List<Category>>
}
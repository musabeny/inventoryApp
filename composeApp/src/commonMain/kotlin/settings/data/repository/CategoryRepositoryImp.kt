package settings.data.repository

import database.InventoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import settings.data.mapper.toCategory
import settings.data.mapper.toCategoryEntity
import settings.domain.model.category.Category
import settings.domain.repository.CategoryRepository

class CategoryRepositoryImp(
    private val db: InventoryDatabase,
):CategoryRepository {
    override suspend fun insertCategory(category: Category): Long {
        return db.categoryDao().insertCategory(category.toCategoryEntity())
    }

    override suspend fun categories(): Flow<List<Category>> {
        return db.categoryDao().getAllCategory().map {
            it.map { category ->
                category.toCategory()
            }
        }
    }

    override suspend fun updateCategory(category: Category): Int {
       return db.categoryDao().updateCategory(category = category.toCategoryEntity())
    }

    override suspend fun deleteCategory(category: Category): Int {
        return db.categoryDao().deleteCategory(category = category.toCategoryEntity())
    }

    override fun searchCategoryByName(name: String): Flow<List<Category>> {
        return db.categoryDao().searchCategoryByName(name = name).map { categories ->
            categories.map { it.toCategory() }
        }
    }
}
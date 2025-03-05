package sales.data.repository

import database.InventoryDatabase
import sales.data.mapper.toTemporaryPrice
import sales.data.mapper.toTemporaryPriceEntity
import sales.domain.model.TemporaryPrice
import sales.domain.repository.SalesRepository

class SalesRepositoryImp(
    private val db:InventoryDatabase,
):SalesRepository {
    override suspend fun saveTemporaryPrice(temporaryPrice: TemporaryPrice) {
        db.temporaryPriceDao().upsetPrice(temporaryPrice.toTemporaryPriceEntity())
    }

    override suspend fun updateTemporaryPrice(temporaryPrice: TemporaryPrice): Int {
      return  db.temporaryPriceDao().updatePrice(temporaryPrice.toTemporaryPriceEntity())
    }

    override suspend fun getTemporaryPrice(code:String): TemporaryPrice? {
       return db.temporaryPriceDao().getTemporaryPrice(code = code)?.toTemporaryPrice()
    }
}
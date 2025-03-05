package sales.domain.repository

import sales.domain.model.TemporaryPrice

interface SalesRepository {
    suspend fun saveTemporaryPrice(temporaryPrice: TemporaryPrice)
    suspend fun updateTemporaryPrice(temporaryPrice: TemporaryPrice):Int
    suspend fun getTemporaryPrice(code:String):TemporaryPrice?
}
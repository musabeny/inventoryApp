package database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import database.entity.TemporaryPriceEntity

@Dao
interface TemporaryPriceDao {

    @Transaction
    suspend fun upsetPrice(priceEntity: TemporaryPriceEntity){
        if(priceEntity.id ==  null){
            savePrice(priceEntity)
        }else{
            updatePrice(priceEntity)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePrice( priceEntity: TemporaryPriceEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePrice(priceEntity: TemporaryPriceEntity):Int

    @Query("SELECT * FROM TemporaryPriceEntity WHERE code = :code")
    suspend fun getTemporaryPrice(code:String): TemporaryPriceEntity?
}
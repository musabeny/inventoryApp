package sales.data.mapper

import database.entity.TemporaryPriceEntity
import sales.domain.model.TemporaryPrice

fun TemporaryPriceEntity.toTemporaryPrice():TemporaryPrice{
    return TemporaryPrice(
        id = id,
        code = code,
        price = price
    )
}

fun  TemporaryPrice.toTemporaryPriceEntity():TemporaryPriceEntity{
    return TemporaryPriceEntity(
        id = id,
        code = code,
        price = price
    )
}
package settings.data.mapper

import database.entity.ProductEntity
import settings.domain.model.product.Product
import settings.domain.model.product.ProductColor

fun ProductEntity.toProduct(
    colors:List<ProductColor>
): Product {
    return Product(
        id = id,
        code = code,
        name = name,
        price = price,
        color = colors.find { it.id == color } ?: ProductColor(),
        barcode = barcode,
        expireDate = expireDate,
        expireDateAlert = expireDateAlert,
        dateCreated = dateCreated
    )
}

fun Product.toProductEntity():ProductEntity{
    return ProductEntity(
        id = id ?: 0L,
        code = code,
        name = name,
        price = price,
        color = color.id,
        barcode = barcode,
        expireDate = expireDate,
        expireDateAlert = expireDateAlert,
    )
}
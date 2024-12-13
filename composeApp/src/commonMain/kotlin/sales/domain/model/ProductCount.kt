package sales.domain.model

import settings.domain.model.product.Product

data class ProductCount(
    val product: Product?,
    val count : Double?
)

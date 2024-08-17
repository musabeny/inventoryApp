package settings.domain.model.category

import androidx.compose.ui.graphics.Color
import settings.domain.model.product.ProductColor

data class CategoryWithColor(
    val id:Long?,
    val name:String,
    val color: ProductColor
)

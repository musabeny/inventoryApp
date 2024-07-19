package settings.domain.model.product

import androidx.compose.ui.graphics.Color

data class ProductColor(
    val id:Int = 1,
    val color: Color = Color.Red,
    val isSelected:Boolean = false
)

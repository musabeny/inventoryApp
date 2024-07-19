package settings.domain.useCase.productUseCase

import androidx.compose.ui.graphics.Color
import settings.domain.model.product.ProductColor

class GetProductColor {
    operator fun invoke():List<ProductColor>{
       return listOf(
           ProductColor(1,Color.Red),
           ProductColor(2,Color.Cyan),
           ProductColor(3,Color.Blue),
           ProductColor(4,Color.Green),
           ProductColor(5,Color.Yellow),
           ProductColor(6,Color.Green),
           ProductColor(7,Color.Magenta),
           ProductColor(8,Color.DarkGray)
       )
    }
}
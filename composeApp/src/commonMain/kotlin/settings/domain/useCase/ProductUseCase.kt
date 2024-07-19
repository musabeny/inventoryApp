package settings.domain.useCase

import settings.domain.useCase.productUseCase.DateMillsToDate
import settings.domain.useCase.productUseCase.GetProductColor
import settings.domain.useCase.productUseCase.ValidateProductForm

data class ProductUseCase(
   val validate:ValidateProductForm = ValidateProductForm(),
   val color: GetProductColor = GetProductColor(),
   val dateMillsToDate:DateMillsToDate = DateMillsToDate()
)

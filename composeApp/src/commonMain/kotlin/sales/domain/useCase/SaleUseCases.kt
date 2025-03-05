package sales.domain.useCase

import sales.domain.useCase.useCases.CalculateItemDetail
import sales.domain.useCase.useCases.CalculateItems
import sales.domain.useCase.useCases.ExtractNumber
import sales.domain.useCase.useCases.MathExpression
import sales.domain.useCase.useCases.ValidateAtBtn
import sales.domain.useCase.useCases.ValidateDotBtn
import sales.domain.useCase.useCases.ValidateZBtn

data class SaleUseCases(
    val expression: MathExpression = MathExpression(),
    val zBtn: ValidateZBtn = ValidateZBtn(),
    val dotBtn: ValidateDotBtn = ValidateDotBtn(),
    val atBtn:ValidateAtBtn = ValidateAtBtn(),
    val extractNumber: ExtractNumber = ExtractNumber(),
    val calculateItemDetail: CalculateItemDetail = CalculateItemDetail()
)

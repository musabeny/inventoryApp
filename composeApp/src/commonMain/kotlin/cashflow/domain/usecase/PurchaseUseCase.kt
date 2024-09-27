package cashflow.domain.usecase

import cashflow.domain.usecase.purchaseCases.ValidateBill
import cashflow.domain.usecase.purchaseCases.ValidateItemAndPrice

data class PurchaseUseCase(
    val validateBill: ValidateBill = ValidateBill(),
    val validateItemAndPrice: ValidateItemAndPrice = ValidateItemAndPrice()
)
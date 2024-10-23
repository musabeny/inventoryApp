package cashflow.presentation.purchaseReceipt

import cashflow.domain.model.purchase.BillAndItems

data class PurchaseReceiptState(
    val billItems: BillAndItems? = null
)

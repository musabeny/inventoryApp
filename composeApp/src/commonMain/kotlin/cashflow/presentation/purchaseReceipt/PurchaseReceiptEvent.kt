package cashflow.presentation.purchaseReceipt

sealed interface PurchaseReceiptEvent {
    data class BillWithItems(val billId:Long?):PurchaseReceiptEvent
    data object NavigateBack:PurchaseReceiptEvent
    data class NavigateToAddPurchase(val billId:Long?):PurchaseReceiptEvent
}
package sales.presentation.payment

import sales.domain.model.ItemDetail

data class PaymentState(
    val items:List<ItemDetail> = emptyList(),
    val total:String = "",
    val showDateDialog:Boolean = false,
    val selectedDate:String = ""
)

package cashflow.domain.model.purchase

data class BillAndItems(
    val bill: Bill,
    val items:List<BillItem>
)

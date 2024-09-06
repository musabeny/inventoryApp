package cashflow.domain.model

data class FilterType(
    val id:Long,
    val label:String,
    val isChecked:Boolean = false
)

package sales.domain.useCase.useCases

import sales.domain.model.ItemDetail

class CalculateItemDetail {
    operator fun invoke(items:List<ItemDetail>):List<ItemDetail>{
      return  items.map { item ->
            item.copy(
                expression = "${item.itemPrice} x ${item.quantity}",
                total = (item.itemPrice ?: 0.0) * (item.quantity ?: 0)
            )
        }
    }
}
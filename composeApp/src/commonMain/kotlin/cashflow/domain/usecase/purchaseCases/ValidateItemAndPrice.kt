package cashflow.domain.usecase.purchaseCases

import cashflow.domain.model.purchase.PurchaseItem
import core.util.UiText
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_atleast_item


class ValidateItemAndPrice {
    operator fun invoke(itemFilled:Boolean): UiText?{
        if(itemFilled){
          return null
        }
        return UiText.StringResources(Res.string.add_atleast_item)
    }
}
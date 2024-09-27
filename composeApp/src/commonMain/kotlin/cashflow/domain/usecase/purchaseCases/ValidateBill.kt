package cashflow.domain.usecase.purchaseCases

import core.util.UiText
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.bill_can_not_empty
import inventoryapp.composeapp.generated.resources.bill_title
import org.jetbrains.compose.resources.StringResource

class ValidateBill {
    operator fun invoke(bill:String):UiText?{
        if(bill.isBlank()){
         return UiText.StringResources(Res.string.bill_can_not_empty)
        }
        return null
    }
}
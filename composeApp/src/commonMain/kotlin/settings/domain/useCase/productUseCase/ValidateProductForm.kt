package settings.domain.useCase.productUseCase

import core.util.isAllDigits
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.can_not_be_empty
import inventoryapp.composeapp.generated.resources.can_not_be_zero
import inventoryapp.composeapp.generated.resources.item_code_must_all_number
import inventoryapp.composeapp.generated.resources.items_about_to_expire
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import settings.domain.enums.ProductForm

class ValidateProductForm {

    operator fun invoke(form: ProductForm, input:String):StringResource?{
        return when(form){
            ProductForm.Code ->{
               if(input.isEmpty()){
                   Res.string.can_not_be_empty
               } else if(!input.isAllDigits()){
                  Res.string.item_code_must_all_number
               } else  if((input.toLongOrNull() ?: 0) <= 0){
                   Res.string.can_not_be_zero
               }else{
                   null
               }
            }
            ProductForm.Price ->{
                if(input.isEmpty()){
                    Res.string.can_not_be_empty
                }else if((input.toDoubleOrNull() ?: 0.0) <=0.0){
                    Res.string.can_not_be_zero
                }else{
                    null
                }
            }
            ProductForm.Name ->{
                if(input.isEmpty()) {
                    Res.string.can_not_be_empty
                }else{
                    null
                }
            }
        }
    }
}
package settings.domain.useCase.categoryUseCase

import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.can_not_be_empty
import inventoryapp.composeapp.generated.resources.category_name_required
import org.jetbrains.compose.resources.StringResource

class ValidateCategoryForm {
    operator fun invoke(input:String):StringResource?{
        return if(input.isBlank()){
            Res.string.category_name_required
        }else{
            null
        }


    }
}
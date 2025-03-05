package sales.domain.useCase.useCases

import core.util.UiText
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.item
import org.jetbrains.compose.resources.getString
import sales.domain.extensions.removeUnnecessaryDecimals
import sales.domain.model.ItemDetail
import sales.domain.useCase.SaleUseCases
import settings.domain.repository.ProductRepository

class CalculateItems(
    private val useCases: SaleUseCases,
    private val productRepository: ProductRepository
) {
   suspend  operator fun invoke(items:List<String>,expression:String,itemSize:Int):ItemDetail?{
       var itemDetail:ItemDetail? = null
        for (item in items){
            if(item.contains('=')){
             val itemPrice =   item.replaceBefore('=',"").replace("=","").trim().toDoubleOrNull()
                val quantity = item.replaceAfter('Z',"").filter { it.isDigit() }.toIntOrNull()
                val total = (itemPrice ?: 0.0) * (quantity ?: 0)
                println("getTempPrice total $total itemPrice  $itemPrice quantity $quantity  ")
                itemDetail = ItemDetail(null,"Item ${itemSize + 1}",total,"$itemPrice x $quantity",itemPrice,quantity)
            }else if(item.contains('Z')){
               val ztype =  item.replaceBefore('Z',"")
               val productId =   useCases.extractNumber(ztype)

               productId?.let {
                   val count = item.replace(ztype,"").toDoubleOrNull()
                   val product =    productRepository.getProductById(it)
                   val total = (count ?:  1.0) * (product?.price ?: 0.0)
                    itemDetail = ItemDetail(product?.id,product?.name,total,"${product?.price ?: ""} x ${count?.removeUnnecessaryDecimals()}", itemPrice = product?.price,count?.removeUnnecessaryDecimals()?.toIntOrNull())
               }

           }else{
                itemDetail = ItemDetail(null,"Item ${itemSize + 1}",item.toDoubleOrNull(),expression,null,null)
           }


        }
     return itemDetail
    }
}
package sales.domain.useCase.useCases

import sales.domain.model.ProductCount
import sales.domain.useCase.SaleUseCases
import settings.domain.repository.ProductRepository

class CalculateItems(
    private val useCases: SaleUseCases,
    private val productRepository: ProductRepository
) {
   suspend  operator fun invoke(items:List<String>):List<ProductCount>{
       val products = mutableListOf<ProductCount>()
        for (item in items){
           if(item.contains('Z')){
               val ztype =  item.replaceBefore('Z',"")
               val productId =   useCases.extractNumber(ztype)
               productId?.let {
                   val count = item.replace(ztype,"").toDoubleOrNull()
                   val product =    productRepository.getProductById(it)
                   val productCount = ProductCount(product,count)
                   products.add(productCount)
               }
           }else{
               val productCount = ProductCount(null,item.toDoubleOrNull())
               products.add(productCount)
           }


        }
     return products.toList()
    }
}
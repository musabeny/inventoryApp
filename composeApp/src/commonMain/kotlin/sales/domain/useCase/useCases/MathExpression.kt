package sales.domain.useCase.useCases

import com.github.murzagalin.evaluator.Evaluator
import sales.domain.extensions.removeUnnecessaryDecimals
import sales.domain.extensions.roundToTwoDecimals

class MathExpression {
    private val evaluator = Evaluator()
    operator fun invoke(expression: String,  itemList: MutableList<String>): Pair<String?,Int?> {
        return try {
            val removeX = expression.replace('x', '*')
            val removeDivide = removeX.replace('÷', '/')
            var productCount:Double? = null;
            var indexOfElement:Int? = null
           val formatExpression = removeZSections(removeDivide)
            val calculate = if(formatExpression.first.isNotEmpty()) evaluator.evaluateDouble(formatExpression.first).roundToTwoDecimals() else 1.0
            formatExpression.second?.let {ztype ->
                val item = itemList.find { it.contains(ztype) }
                item?.let {product ->
                    indexOfElement =  itemList.indexOf(product)
                   productCount =  product.replace(ztype,"").trim().toDoubleOrNull()
                }

            }
           val result =  calculate.plus(productCount ?: 0.0)
            Pair("${result.removeUnnecessaryDecimals()}${formatExpression.second ?: ""}",indexOfElement)
        } catch (e: Exception) {
            println("calculation ${e.message}")
            Pair(null,null)
        }
    }

    private fun removeZSections(expression: String): Pair<String,String?> {
         val zType = expression.split('+','-','*','/').find { it.contains('Z') }
        if(zType == null){
            return  Pair(expression,null)
        }else{
         val index =   expression.indexOf(zType)
           val startIndex = if(index > 0) index - 1 else index
           val endIndex = startIndex + zType.length
            val result = expression.removeRange(startIndex,if(index > 0) endIndex+1 else endIndex)
            return  Pair(result.dropWhile { !it.isLetterOrDigit() },zType)
        }


    }

}

package sales.domain.useCase.useCases

class ValidateDotBtn {
    operator fun invoke(value:StringBuilder,isEnterPrice:Boolean = false):Boolean{
//        if(value.isEmpty()){
//            return false
//        }
        return if(isEnterPrice){
            println("validate dot ${value.any { it == '.' }}")
            value.any { it == '.' }
        }else{
            when{
                value.lastOrNull() == '.' -> false
                !hasValidDots(value)  -> false
                else -> true
            }
        }

    }

    private fun hasValidDots(expression: StringBuilder): Boolean {
        val segments =  expression.split('+','-','x','÷')
       return when{
           segments.lastOrNull()?.any { it == 'Z' } == true -> false
           (segments.lastOrNull()?.count { it == '.' } ?: 0) >= 1 -> false
           else -> true
       }
    }
}
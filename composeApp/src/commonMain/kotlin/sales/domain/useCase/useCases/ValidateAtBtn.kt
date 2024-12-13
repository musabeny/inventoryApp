package sales.domain.useCase.useCases

class ValidateAtBtn {
    operator fun invoke(value:StringBuilder):Pair<Boolean,String?>{
      return hasValidZ(value)
    }
    private fun hasValidZ(expression: StringBuilder): Pair<Boolean,String?> {
        if(expression.isEmpty()){
            return Pair(false,null)
        }
        val segments =  expression.split('+','-','x','÷')
        return if( segments.lastOrNull() != null){
            when{
                segments.last().lastOrNull()?.isDigit() == true && segments.last().firstOrNull() == 'Z' ->  Pair(true,segments.last())
                segments.last().isEmpty() && segments[segments.lastIndex-1].lastOrNull()?.isDigit()  == true && segments[segments.lastIndex-1].firstOrNull() == 'Z' -> Pair(true,segments[segments.lastIndex-1])
                else ->  Pair(false,null)
            }
        }else{
            Pair(false,null)
        }

    }

}
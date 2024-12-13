package sales.domain.useCase.useCases

class ValidateZBtn {
    operator fun invoke(value:StringBuilder):Boolean{
      return when{
          value.any { it == '+' || it == '-' || it == 'Z' } -> false
          value.lastOrNull()?.isDigit() == false -> false
          else -> true
       }
    }
}
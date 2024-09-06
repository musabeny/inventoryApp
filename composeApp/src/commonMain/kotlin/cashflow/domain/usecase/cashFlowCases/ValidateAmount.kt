package cashflow.domain.usecase.cashFlowCases

class ValidateAmount {

    operator fun invoke(amount:String):Boolean{
        if (amount.isEmpty()) {
            return false
        }

        // Check if the first character is a digit

        if (!amount[0].isDigit()) {
            return false
        }

        // Count the number of dots
        val dotCount = amount.count { it == '.' }
        if (dotCount > 1) {
            return false
        }

        // Check if all remaining characters are digits or dots
        return amount.substring(1).matches(Regex("[0-9.]*"))
    }
}
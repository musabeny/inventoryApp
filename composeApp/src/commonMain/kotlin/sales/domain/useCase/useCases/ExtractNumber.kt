package sales.domain.useCase.useCases

class ExtractNumber {
    operator fun invoke(input: String): Long? {
        if(input.isEmpty()){
            return null
        }
        val regex = "\\d+".toRegex() // Matches one or more digits
        val match = regex.find(input)
        return match?.value?.toLongOrNull()
    }
}
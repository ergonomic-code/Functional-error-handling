package pro.azhidkov.training.functional_errors

sealed interface ParseResult {
    @JvmInline
    value class Success(val value: Int) : ParseResult
    data class Failure(val pos: Int, val char: Char) : ParseResult
}

object CustomAdt {

    fun parseInt(input: String): ParseResult {
        val (sign, elementsToSkip) = if (input.startsWith("-")) -1 to 1 else 1 to 0

        return input.withIndex()
            .drop(elementsToSkip)
            .map { (index, char) ->
                if (char in '0'..'9') {
                    char - '0'
                } else {
                    return ParseResult.Failure(index, char)
                }
            }
            .fold(0) { acc, digit -> acc * 10 + digit }
            .let { result -> ParseResult.Success(result * sign) }
    }

}
package pro.azhidkov.training.functional_errors

sealed interface ParseResult {
    @JvmInline
    value class Success(val value: Int) : ParseResult
    data class Failure(val pos: Int, val char: Char) : ParseResult
}

object CustomAdt {

    fun parseInt(str: String): ParseResult {
        var res = 0
        val sign = if (str.startsWith("-")) -1 else 1
        val symbolsToSkip = if (sign == -1) 1 else 0

        for ((i, char) in str.withIndex().drop(symbolsToSkip)) {
            if (char !in '0'..'9')
                return ParseResult.Failure(i, char)

            res = res * 10 + (char - '0')
        }

        return ParseResult.Success(res * sign)
    }

}
package pro.azhidkov.training.functional_errors

sealed interface ParseResult {
    @JvmInline
    value class Success(val value: Int) : ParseResult
    data class Failure(val pos: Int, val char: Char) : ParseResult
}

object CustomAdt {

    fun parseInt(str: String): ParseResult {
        if (str.startsWith("--")) return ParseResult.Failure(1, '-')

        try {
            return ParseResult.Success(str.toInt())
        } catch (_: NumberFormatException) {
            val pos = str.indexOfFirst { ch -> !ch.isDigit() }
            return ParseResult.Failure(pos, str[pos])
        }
    }
}
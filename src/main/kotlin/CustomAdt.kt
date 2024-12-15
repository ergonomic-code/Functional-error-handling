package pro.azhidkov.training.functional_errors

sealed interface ParseResult {
    @JvmInline
    value class Success(val value: Int) : ParseResult
    data class Failure(val pos: Int, val char: Char) : ParseResult
}

fun String.firstNonDigitCharOrNull(): IndexedValue<Char>? =
    this
        .asSequence()
        .withIndex()
        .filterNot { isCorrectIntStringChar(it.index, it.value, this.first()) }
        .firstOrNull()

fun isCorrectIntStringChar(pos: Int, char: Char, firstChar: Char): Boolean =
    when {
        pos == 0 && char == '-' -> true
        pos == 1 && char == '0' && firstChar == '-' -> false
        pos == 1 && char == '0' && firstChar == '0' -> false
        (pos < 10 || (firstChar == '-' && pos == 10)) && char.isDigit() -> true
        else -> false
    }

fun parseCorrectInt(str: String): Int =
    when (str.first()) {
        '-' -> parseNegativeInt(str)
        else -> parsePositiveInt(str)
    }

fun parseNegativeInt(str: String): Int =
    -1 * parsePositiveInt(str.drop(1))

fun parsePositiveInt(str: String): Int =
    str
        .map { it - '0' }
        .fold(0) { accumulator, digit -> accumulator * 10 + digit }

object CustomAdt {

    fun parseInt(str: String): ParseResult {
        return when {
            str.isEmpty() -> ParseResult.Failure(-1, '\u0000')
            else -> str.firstNonDigitCharOrNull()
                ?.let { ParseResult.Failure(it.index, it.value) }
                ?: ParseResult.Success(parseCorrectInt(str))
        }
    }

}
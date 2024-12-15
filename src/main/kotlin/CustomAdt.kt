package pro.azhidkov.training.functional_errors

import kotlin.math.pow

sealed interface ParseResult {
    @JvmInline
    value class Success(val value: Int) : ParseResult
    data class Failure(val pos: Int, val char: Char) : ParseResult
}

sealed interface ValidationResult {
    @JvmInline
    value class Success(val value: String) : ValidationResult
    data class Failure(val pos: Int, val char: Char) : ValidationResult
}

data class Digit(val decimalRadix: Int, val char: Char) {
    val value: Int
        get() = char - '0'
}

fun validateIntString(str: String): ValidationResult =
    str
        .asSequence()
        .mapIndexedNotNull { index, char ->
            when {
                isCorrectIntStringChar(index, char, str.first()) -> null
                else -> ValidationResult.Failure(index, char)
            }
        }
        .firstOrNull()
        ?: ValidationResult.Success(str)

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

fun parseNegativeInt(str: String): Int = -1 * parsePositiveInt(str.drop(1))

fun parsePositiveInt(str: String): Int = str
    .mapIndexed { index, digit -> Digit((10.0.pow(str.length - index - 1)).toInt(), digit) }
    .sumOf { it.value * it.decimalRadix }

object CustomAdt {

    fun parseInt(str: String): ParseResult {
        return when {
            str.isEmpty() -> ParseResult.Failure(-1, '\u0000')
            else -> when (val validationResult = validateIntString(str)) {
                is ValidationResult.Success -> ParseResult.Success(parseCorrectInt(validationResult.value))
                is ValidationResult.Failure -> ParseResult.Failure(validationResult.pos, validationResult.char)
            }
        }
    }

}
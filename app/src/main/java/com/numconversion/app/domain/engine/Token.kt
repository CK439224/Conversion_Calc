package com.numconversion.app.domain.engine

import java.math.BigDecimal

sealed class Token {
    data class Number(val value: BigDecimal) : Token()
    data object Plus : Token()
    data object Minus : Token()
    data object Multiply : Token()
    data object Divide : Token()
    data object LParen : Token()
    data object RParen : Token()
}

class TokenizeException(message: String) : Exception(message)

object Tokenizer {

    /** Accepts both ASCII and the calculator's display glyphs (× ÷) as operators. */
    fun tokenize(expression: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        while (i < expression.length) {
            val c = expression[i]
            when {
                c.isWhitespace() -> i++
                c.isDigit() || c == '.' -> {
                    val start = i
                    var seenDot = c == '.'
                    i++
                    while (i < expression.length &&
                        (expression[i].isDigit() || (expression[i] == '.' && !seenDot))
                    ) {
                        if (expression[i] == '.') seenDot = true
                        i++
                    }
                    val text = expression.substring(start, i)
                    if (text == ".") throw TokenizeException("Invalid number")
                    tokens.add(Token.Number(BigDecimal(text)))
                }
                c == '+' -> { tokens.add(Token.Plus); i++ }
                c == '-' -> { tokens.add(Token.Minus); i++ }
                c == '*' || c == '×' -> { tokens.add(Token.Multiply); i++ }
                c == '/' || c == '÷' -> { tokens.add(Token.Divide); i++ }
                c == '(' -> { tokens.add(Token.LParen); i++ }
                c == ')' -> { tokens.add(Token.RParen); i++ }
                else -> throw TokenizeException("Unexpected character '$c'")
            }
        }
        return tokens
    }
}

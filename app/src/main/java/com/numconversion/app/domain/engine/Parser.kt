package com.numconversion.app.domain.engine

class ParseException(message: String) : Exception(message)

/**
 * Recursive-descent parser over the standard precedence grammar:
 *   expression := term (('+' | '-') term)*
 *   term       := unary (('*' | '/') unary)*
 *   unary      := '-' unary | '+' unary | primary
 *   primary    := NUMBER | '(' expression ')'
 */
class Parser private constructor(private val tokens: List<Token>) {
    private var pos = 0

    companion object {
        fun parse(tokens: List<Token>): AstNode {
            if (tokens.isEmpty()) throw ParseException("Empty expression")
            val parser = Parser(tokens)
            val node = parser.parseExpression()
            if (!parser.isAtEnd()) throw ParseException("Unexpected token")
            return node
        }
    }

    private fun peek(): Token? = tokens.getOrNull(pos)
    private fun isAtEnd(): Boolean = pos >= tokens.size
    private fun advance(): Token = tokens[pos++]

    private fun parseExpression(): AstNode {
        var node = parseTerm()
        while (true) {
            node = when (peek()) {
                Token.Plus -> { advance(); AstNode.BinaryOp(node, Operator.ADD, parseTerm()) }
                Token.Minus -> { advance(); AstNode.BinaryOp(node, Operator.SUBTRACT, parseTerm()) }
                else -> return node
            }
        }
    }

    private fun parseTerm(): AstNode {
        var node = parseUnary()
        while (true) {
            node = when (peek()) {
                Token.Multiply -> { advance(); AstNode.BinaryOp(node, Operator.MULTIPLY, parseUnary()) }
                Token.Divide -> { advance(); AstNode.BinaryOp(node, Operator.DIVIDE, parseUnary()) }
                else -> return node
            }
        }
    }

    private fun parseUnary(): AstNode {
        if (peek() == Token.Minus) {
            advance()
            return AstNode.UnaryMinus(parseUnary())
        }
        if (peek() == Token.Plus) {
            advance()
            return parseUnary()
        }
        return parsePrimary()
    }

    private fun parsePrimary(): AstNode {
        val token = peek() ?: throw ParseException("Unexpected end of expression")
        return when (token) {
            is Token.Number -> { advance(); AstNode.Number(token.value) }
            Token.LParen -> {
                advance()
                val node = parseExpression()
                if (peek() != Token.RParen) throw ParseException("Missing closing parenthesis")
                advance()
                node
            }
            else -> throw ParseException("Unexpected token")
        }
    }
}

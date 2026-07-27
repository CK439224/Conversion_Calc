package com.numconversion.app.domain.engine

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

sealed class CalculatorResult {
    data class Value(val value: BigDecimal) : CalculatorResult()
    data class Error(val message: String) : CalculatorResult()
}

/** Evaluates arithmetic expressions over BigDecimal, never Double, to avoid float rounding artifacts. */
object Evaluator {
    private val DIVISION_CONTEXT = MathContext(34, RoundingMode.HALF_UP)

    fun evaluate(expression: String): CalculatorResult {
        return try {
            val tokens = Tokenizer.tokenize(expression)
            val ast = Parser.parse(tokens)
            CalculatorResult.Value(normalize(evaluateNode(ast)))
        } catch (e: ArithmeticException) {
            CalculatorResult.Error(e.message ?: "Cannot divide by zero")
        } catch (e: TokenizeException) {
            CalculatorResult.Error(e.message ?: "Invalid expression")
        } catch (e: ParseException) {
            CalculatorResult.Error(e.message ?: "Invalid expression")
        }
    }

    private fun evaluateNode(node: AstNode): BigDecimal = when (node) {
        is AstNode.Number -> node.value
        is AstNode.UnaryMinus -> evaluateNode(node.operand).negate()
        is AstNode.BinaryOp -> {
            val left = evaluateNode(node.left)
            val right = evaluateNode(node.right)
            when (node.op) {
                Operator.ADD -> left.add(right)
                Operator.SUBTRACT -> left.subtract(right)
                Operator.MULTIPLY -> left.multiply(right)
                Operator.DIVIDE -> {
                    if (right.compareTo(BigDecimal.ZERO) == 0) {
                        throw ArithmeticException("Cannot divide by zero")
                    }
                    left.divide(right, DIVISION_CONTEXT)
                }
            }
        }
    }

    /** Strips trailing zeros but avoids collapsing into scientific-notation scale. */
    private fun normalize(value: BigDecimal): BigDecimal {
        val stripped = value.stripTrailingZeros()
        return if (stripped.scale() < 0) stripped.setScale(0) else stripped
    }
}

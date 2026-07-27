package com.numconversion.app.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EvaluatorTest {

    private fun assertValue(expected: String, expression: String) {
        val result = Evaluator.evaluate(expression)
        assertTrue("Expected Value but got $result for '$expression'", result is CalculatorResult.Value)
        assertEquals(expected, (result as CalculatorResult.Value).value.toPlainString())
    }

    private fun assertError(expression: String) {
        val result = Evaluator.evaluate(expression)
        assertTrue("Expected Error but got $result for '$expression'", result is CalculatorResult.Error)
    }

    @Test fun `simple addition`() = assertValue("5", "2+3")

    @Test fun `multiplication before addition`() = assertValue("14", "2+3*4")

    @Test fun `parentheses override precedence`() = assertValue("20", "(2+3)*4")

    @Test fun `division`() = assertValue("2.5", "5/2")

    @Test fun `subtraction`() = assertValue("-1", "2-3")

    @Test fun `nested parentheses`() = assertValue("1", "((1+2)-3)+1")

    @Test fun `unary minus on a leading number`() = assertValue("-5", "-5")

    @Test fun `unary minus inside parentheses`() = assertValue("-6", "3*(-2+0)")

    @Test fun `decimal arithmetic avoids float artifacts`() = assertValue("0.3", "0.1+0.2")

    @Test fun `display glyphs times and divide are accepted`() = assertValue("6", "2×3")

    @Test fun `unicode divide glyph is accepted`() = assertValue("2", "4÷2")

    @Test fun `division by zero is an error`() = assertError("5/0")

    @Test fun `empty expression is an error`() = assertError("")

    @Test fun `unmatched opening parenthesis is an error`() = assertError("(1+2")

    @Test fun `unmatched closing parenthesis is an error`() = assertError("1+2)")

    @Test fun `trailing operator is an error`() = assertError("1+")

    @Test fun `two consecutive operators is an error`() = assertError("1*/2")
}

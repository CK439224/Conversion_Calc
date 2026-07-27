package com.numconversion.app.domain.engine

import java.math.BigDecimal

sealed class AstNode {
    data class Number(val value: BigDecimal) : AstNode()
    data class UnaryMinus(val operand: AstNode) : AstNode()
    data class BinaryOp(val left: AstNode, val op: Operator, val right: AstNode) : AstNode()
}

enum class Operator { ADD, SUBTRACT, MULTIPLY, DIVIDE }

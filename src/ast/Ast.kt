package ast

sealed class Statement

data class VariableDeclaration(val name: String, val value: Expression) : Statement()
data class PrintStatement(val expression: Expression) : Statement()
data class IfStatement(val condition: Expression, val ifBranch: Block, val elseBranch: Block?) : Statement()
data class FunctionDefinition(
    val name: String,
    val parameters: List<String>,
    val body: Block,
    val returnStatement: ReturnStatement?
) : Statement()
data class Block(val statements: List<Statement>)
data class ReturnStatement(val expression: Expression) : Statement()

sealed class Expression
data class Number(val value: Int) : Expression()
data class Variable(val name: String) : Expression()
data class BinaryOperation(val left: Expression, val operator: String, val right: Expression) : Expression()
data class FunctionCall(val functionName: String, val arguments: List<Expression>) : Expression()
data class ListExpression(val elements: List<Expression>) : Expression()
data class StringLiteral(val value: String) : Expression()

data class Program(val statements: List<Statement>)
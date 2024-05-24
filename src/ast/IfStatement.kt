package ast

data class IfStatement(val condition: Expression, val thenBranch: List<Statement>, val elseBranch: List<Statement>? = null) : Statement()
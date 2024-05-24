package ast

data class VariableDeclaration(val name: String, val value: Expression) : Statement()
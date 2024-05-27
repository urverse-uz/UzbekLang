package interpreter

import ast.*
import ast.Number

class Interpreter(private val program: Program) {
    private val variables = mutableMapOf<String, Any>()
    private val functions = mutableMapOf<String, (List<Int>) -> Int>()
    private var returnValue: Int? = null

    init {
        // Define built-in functions here if necessary
        functions["qoshish"] = { args -> args[0] + args[1] }
    }

    fun interpret() {
        program.statements.forEach { executeStatement(it) }
    }

    private fun executeStatement(statement: Statement) {
        when (statement) {
            is VariableDeclaration -> {
                variables[statement.name] = evaluateExpression(statement.value)
            }


            is PrintStatement -> {
                val value = evaluateExpression(statement.expression)
                println(value)
            }

            is IfStatement -> {
                val condition = evaluateExpression(statement.condition)
                if (condition != 0) {
                    executeBlock(statement.ifBranch)
                } else if (statement.elseBranch != null) {
                    executeBlock(statement.elseBranch)
                }
            }

            is FunctionDefinition -> {
                functions[statement.name] = { args ->
                    // Create a new scope for function execution
                    val previousVariables = variables.toMap()
                    statement.parameters.forEachIndexed { index, param ->
                        variables[param] = args[index]
                    }
                    executeBlock(statement.body)
                    // Capture the return value
                    val result = returnValue ?: 0
                    returnValue = null
                    // Restore previous variable scope
                    variables.clear()
                    variables.putAll(previousVariables)
                    // Return value (if any)
                    result
                }
            }

            is ReturnStatement -> {
                returnValue = evaluateExpression(statement.expression) as Int
            }

        }
    }

    private fun executeBlock(block: Block) {
        block.statements.forEach {
            executeStatement(it)
            if (returnValue != null) {
                return
            }
        }
    }

    private fun evaluateExpression(expression: Expression): Any {
        return when (expression) {
            is Number -> expression.value
            is Variable -> {
                when (val variableName = expression.name) {
                    "t" -> true
                    "nt" -> false
                    else -> {
                        when (val variableValue = variables[variableName]) {
                            is Int -> variableValue
                            is String -> variableValue
                            is Boolean -> variableValue
                            else -> throw IllegalArgumentException("Unsupported variable type: $variableValue")
                        }
                    }
                }
            }

            is BinaryOperation -> {
                val left = evaluateExpression(expression.left)
                val right = evaluateExpression(expression.right)
                when (expression.operator) {
                    "+" -> (left as Int) + (right as Int)
                    "-" -> (left as Int) - (right as Int)
                    "*" -> (left as Int) * (right as Int)
                    "/" -> (left as Int) / (right as Int)
                    "==" -> left == right
                    "!=" -> left != right
                    else -> throw IllegalArgumentException("Unknown operator: ${expression.operator}")
                }
            }

            is FunctionCall -> {
                val function = functions[expression.functionName]
                    ?: throw IllegalArgumentException("Unknown function: ${expression.functionName}")
                val arguments = expression.arguments.map { evaluateExpression(it) as Int }
                function(arguments)
            }

            is ListExpression -> expression.elements.size

            is StringLiteral -> expression.value
            is BooleanLiteral -> expression.value

            else -> throw IllegalArgumentException("Unknown expression type: $expression")
        }
    }


}

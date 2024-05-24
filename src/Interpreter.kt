class Interpreter(private val program: Program) {
    private val variables = mutableMapOf<String, Int>()

    fun interpret() {
        for (statement in program.statements) {
            execute(statement)
        }
    }

    private fun execute(statement: Statement) {
        when (statement) {
            is VariableDeclaration -> {
                val value = evaluate(statement.value)
                variables[statement.name] = value
            }
            is PrintStatement -> {
                val value = evaluate(statement.expression)
                println(value)
            }
        }
    }

    private fun evaluate(expression: Expression): Int {
        return when (expression) {
            is Number -> expression.value
            is Variable -> variables[expression.name] ?: throw IllegalArgumentException("Undefined variable: ${expression.name}")
            is BinaryOperation -> {
                val left = evaluate(expression.left)
                val right = evaluate(expression.right)
                when (expression.operator) {
                    "+" -> left + right
                    "-" -> left - right
                    "*" -> left * right
                    "/" -> left / right
                    else -> throw IllegalArgumentException("Unknown operator: ${expression.operator}")
                }
            }
        }
    }
}

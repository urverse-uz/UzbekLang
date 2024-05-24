







class Parser(private val tokens: List<Token>) {
    private var position: Int = 0

    private fun currentToken(): Token = tokens.getOrElse(position) { Token(TokenType.EOF, "") }

    private fun advance() {
        if (position < tokens.size) {
            position++
        }
    }

    private fun consume(type: TokenType, value: String? = null, endOfInputAllowed: Boolean = false): Token {
        val token = currentToken()
        if (token.type == type && (value == null || token.value == value)) {
            advance()
            return token
        } else if (endOfInputAllowed && token.type == TokenType.EOF) {
            return token
        }
        throw IllegalArgumentException("Expected $type with value $value but got ${token.type} with value ${token.value}")
    }


    fun parse(): Program {
        val statements = mutableListOf<Statement>()
        while (currentToken().type != TokenType.EOF) {
            statements.add(parseStatement())
        }
        return Program(statements)
    }

    private fun parseStatement(): Statement {
        return when (currentToken().value) {
            "ozgaruvchi" -> parseVariableDeclaration()
            "chiqar" -> parsePrintStatement()
            else -> throw IllegalArgumentException("Unexpected token: ${currentToken().value}")
        }
    }

    private fun parseVariableDeclaration(): Statement {
        consume(TokenType.KEYWORD, "ozgaruvchi")
        val name = consume(TokenType.IDENTIFIER).value
        consume(TokenType.SYMBOL, "->")
        val value = parseExpression()
        consume(TokenType.SYMBOL, ";", true) // Allow for end of input as well
        return VariableDeclaration(name, value)
    }

    private fun parsePrintStatement(): Statement {
        consume(TokenType.KEYWORD, "chiqar")
        val expression = parseExpression()
        consume(TokenType.SYMBOL, ";", true) // Allow for end of input as well
        return PrintStatement(expression)
    }



    private fun parseExpression(): Expression {
        var result = parseTerm()
        while (currentToken().value in listOf("+", "-")) {
            val operator = currentToken().value
            advance()
            val right = parseTerm()
            result = BinaryOperation(result, operator, right)
        }
        return result
    }

    private fun parseTerm(): Expression {
        var result = parseFactor()
        while (currentToken().value in listOf("*", "/")) {
            val operator = currentToken().value
            advance()
            val right = parseFactor()
            result = BinaryOperation(result, operator, right)
        }
        return result
    }

    private fun parseFactor(): Expression {
        return when (currentToken().type) {
            TokenType.NUMBER -> {
                val value = currentToken().value.toInt()
                advance()
                Number(value)
            }
            TokenType.IDENTIFIER -> {
                val name = currentToken().value
                advance()
                Variable(name)
            }
            TokenType.SYMBOL -> {
                if (currentToken().value == "(") {
                    advance()
                    val expression = parseExpression()
                    consume(TokenType.SYMBOL, ")")
                    expression
                } else {
                    throw IllegalArgumentException("Unexpected symbol: ${currentToken().value}")
                }
            }
            else -> throw IllegalArgumentException("Unexpected token: ${currentToken().value}")
        }
    }
}

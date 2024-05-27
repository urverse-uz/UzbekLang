// parser/Parser.kt
package parser

import ast.*
import lexer.Token
import lexer.TokenType

class Parser(private val tokens: List<Token>) {
    private var position: Int = 0

    private fun currentToken(): Token = tokens.getOrElse(position) { Token(TokenType.EOF, "") }

    private fun advance() {
        if (position < tokens.size) {
            position++
        }
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
            "agar" -> parseIfStatement()
            "f" -> parseFunctionDefinition()
            "qaytar" -> parseReturnStatement()
            else -> throw IllegalArgumentException("Unexpected token: ${currentToken().value}")
        }
    }

    private fun parseReturnStatement(): ReturnStatement {
        advance() // consume "qaytar"
        val expression = parseExpression()
        if (currentToken().value != ";") {
            throw IllegalArgumentException("Expected ';' after return statement")
        }
        advance() // consume ";"
        return ReturnStatement(expression)
    }

    private fun parseVariableDeclaration(): VariableDeclaration {
        advance() // consume "ozgaruvchi"
        val name = currentToken().value
        advance() // consume variable name
        if (currentToken().value != "->") {
            throw IllegalArgumentException("Expected '->' after variable name")
        }
        advance() // consume "->"
        val value = parseExpression()
        if (currentToken().value != ";") {
            throw IllegalArgumentException("Expected ';' after variable declaration")
        }
        advance() // consume ";"
        return VariableDeclaration(name, value)
    }

    private fun parsePrintStatement(): PrintStatement {
        advance() // consume "chiqar"
        val expression = parseExpression()
        if (currentToken().value != ";") {
            throw IllegalArgumentException("Expected ';' after print statement")
        }
        advance() // consume ";"
        return PrintStatement(expression)
    }

    private fun parseIfStatement(): IfStatement {
        advance() // consume "agar"
        if (currentToken().value != "(") {
            throw IllegalArgumentException("Expected '(' after 'agar'")
        }
        advance() // consume "("
        val condition = parseExpression()
        if (currentToken().value != ")") {
            throw IllegalArgumentException("Expected ')' after condition")
        }
        advance() // consume ")"
        if (currentToken().value != "{") {
            throw IllegalArgumentException("Expected '{' after ')'")
        }
        val ifBranch = parseBlock()
        var elseBranch: Block? = null
        if (currentToken().value == "yoki") {
            advance() // consume "yoki"
            elseBranch = parseBlock()
        }
        return IfStatement(condition, ifBranch, elseBranch)
    }

    private fun parseBlock(): Block {
        val statements = mutableListOf<Statement>()
        if (currentToken().value != "{") {
            throw IllegalArgumentException("Expected '{' to start block")
        }
        advance() // consume "{"
        while (currentToken().value != "}") {
            statements.add(parseStatement())
        }
        advance() // consume "}"
        return Block(statements)
    }

    private fun parseFunctionDefinition(): FunctionDefinition {
        advance() // consume "f"
        val name = currentToken().value
        advance() // consume function name
        if (currentToken().value != "(") {
            throw IllegalArgumentException("Expected '(' after function name")
        }
        advance() // consume "("
        val parameters = mutableListOf<String>()
        while (currentToken().value != ")") {
            parameters.add(currentToken().value)
            advance() // consume parameter
            if (currentToken().value == "|") {
                advance() // consume "|"
            }
        }
        advance() // consume ")"
        val body = parseBlock()
        var returnStatement: ReturnStatement? = null
        if (currentToken().value == "qaytar") {
            returnStatement = parseReturnStatement()
        }
        return FunctionDefinition(name, parameters, body, returnStatement)
    }


    private fun parseExpression(): Expression {
        return parseBinaryOperation()
    }

    private fun parseBinaryOperation(): Expression {
        var left = parsePrimary()
        while (currentToken().value in listOf("+", "-", "*", "/", ">", "<", "==")) {
            val operator = currentToken().value
            advance() // consume operator
            val right = parsePrimary()
            left = BinaryOperation(left, operator, right)
        }
        return left
    }

    private fun parsePrimary(): Expression {
        return when (currentToken().type) {
            TokenType.NUMBER -> {
                val value = currentToken().value.toInt()
                advance() // consume number
                Number(value)
            }
            TokenType.IDENTIFIER -> {
                val name = currentToken().value
                advance() // consume identifier
                if (currentToken().value == "(") {
                    advance() // consume "("
                    val arguments = mutableListOf<Expression>()
                    while (currentToken().value != ")") {
                        arguments.add(parseExpression())
                        if (currentToken().value == "|") {
                            advance() // consume "|"
                        }
                    }
                    advance() // consume ")"
                    FunctionCall(name, arguments)
                } else {
                    Variable(name)
                }
            }
            TokenType.SYMBOL -> {
                when (currentToken().value) {
                    "[" -> parseListExpression()
                    else -> throw IllegalArgumentException("Unexpected symbol: ${currentToken().value}")
                }
            }
            TokenType.STRING -> {
                val value = currentToken().value
                advance() // consume string
                StringLiteral(value)
            }
            else -> throw IllegalArgumentException("Unexpected token: ${currentToken().value}")
        }
    }

    private fun parseListExpression(): ListExpression {
        val elements = mutableListOf<Expression>()
        advance() // consume "["
        while (currentToken().value != "]") {
            elements.add(parseExpression())
            if (currentToken().value == "|") {
                advance() // consume "|"
            }
        }
        advance() // consume "]"
        return ListExpression(elements)
    }
}

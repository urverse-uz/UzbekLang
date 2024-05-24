package lexer

class Lexer(private val input: String) {
    private var position: Int = 0

    private val keywords = mapOf(
        "ozgaruvchi" to TokenType.KEYWORD,
        "chiqar" to TokenType.KEYWORD
    )

    private fun currentChar(): Char? = input.getOrNull(position)

    private fun advance() {
        position++
    }

    private fun skipWhitespace() {
        while (currentChar()?.isWhitespace() == true) {
            advance()
        }
    }

    private fun identifier(): Token {
        val start = position
        while (currentChar()?.isLetterOrDigit() == true) {
            advance()
        }
        val value = input.substring(start, position)
        val type = keywords.getOrDefault(value, TokenType.IDENTIFIER)
        return Token(type, value)
    }

    private fun number(): Token {
        val start = position
        while (currentChar()?.isDigit() == true) {
            advance()
        }
        val value = input.substring(start, position)
        return Token(TokenType.NUMBER, value)
    }

    private fun symbol(): Token {
        val current = currentChar() ?: throw IllegalArgumentException("Unexpected end of input")
        val next = input.getOrNull(position + 1)

        return when {
            current == '-' && next == '>' -> {
                advance()
                advance()
                Token(TokenType.SYMBOL, "->")
            }
            current == '+' -> {
                advance()
                Token(TokenType.SYMBOL, "+")
            }
            current == '-' -> {
                advance()
                Token(TokenType.SYMBOL, "-")
            }
            current == '*' -> {
                advance()
                Token(TokenType.SYMBOL, "*")
            }
            current == '/' -> {
                advance()
                Token(TokenType.SYMBOL, "/")
            }
            current == '(' -> {
                advance()
                Token(TokenType.SYMBOL, "(")
            }
            current == ')' -> {
                advance()
                Token(TokenType.SYMBOL, ")")
            }
            current == ';' -> {
                advance()
                Token(TokenType.SYMBOL, ";")
            }
            else -> throw IllegalArgumentException("Unexpected character: $current")
        }
    }

    fun getNextToken(): Token {
        skipWhitespace()

        val current = currentChar() ?: return Token(TokenType.EOF, "")

        return when {
            current == '/' && input.getOrNull(position + 1) == '/' -> {
                while (currentChar() != '\n' && currentChar() != null) {
                    advance()
                }
                getNextToken()
            }
            current.isLetter() -> identifier()
            current.isDigit() -> number()
            current == ':' -> symbol()
            current == '(' || current == ')' || current == '+' || current == '-' || current == '*' || current == ';' -> symbol()
            else -> throw IllegalArgumentException("Unexpected character: $current")
        }
    }

}
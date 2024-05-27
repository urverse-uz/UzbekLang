package lexer

class Lexer(private val input: String) {
    private var position: Int = 0

    private val keywords = mapOf(
        "ozgaruvchi" to TokenType.KEYWORD,
        "chiqar" to TokenType.KEYWORD,
        "agar" to TokenType.KEYWORD,
        "yoki" to TokenType.KEYWORD,
        "f" to TokenType.KEYWORD,
        "qaytar" to TokenType.KEYWORD,
        "Royxat" to TokenType.KEYWORD
    )

    fun getNextToken(): Token {
        while (position < input.length) {
            val currentChar = input[position]

            if (currentChar == '#' && input.getOrNull(position + 1) == '#') {
                // Single-line comment, ignore characters until the end of the line
                while (position < input.length && input[position] != '\n') {
                    position++
                }
                // Skip newline character if present
                if (position < input.length && input[position] == '\n') {
                    position++
                }
                continue
            }

            if (currentChar.isWhitespace()) {
                position++
                continue
            }

            if (currentChar.isDigit()) {
                val number = extractNumber()
                return Token(TokenType.NUMBER, number)
            }

            if (currentChar.isLetter()) {
                val identifier = extractIdentifier()
                val keywordType = keywords[identifier]
                return if (keywordType != null) {
                    Token(keywordType, identifier)
                } else {
                    Token(TokenType.IDENTIFIER, identifier)
                }
            }

            if (currentChar == '"') {
                val stringLiteral = extractStringLiteral()
                return Token(TokenType.STRING, stringLiteral) // Return STRING token type
            }


            // Handle symbols
            val symbol = when (currentChar) {

                '(' -> {
                    position++
                    "("
                }

                ')' -> {
                    position++
                    ")"
                }

                '+' -> {
                    position++
                    "+"
                }

                '-' -> {
                    if (input.getOrNull(position + 1) == '>') {
                        position += 2
                        "->"
                    } else {
                        position++
                        "-"
                    }
                }

                '*' -> {
                    position++
                    "*"
                }

                '/' -> {
                    position++
                    "/"
                }

                ';' -> {
                    position++
                    ";"
                }

                '>' -> {
                    position++
                    ">"
                }

                '<' -> {
                    position++
                    ">"
                }


                '=' -> {
                    if (input.getOrNull(position + 1) == '=') {
                        position += 2
                        "=="
                    } else {
                        position++
                        throw IllegalArgumentException("Unexpected character: $currentChar")
                    }
                }

                '!' -> {
                    if (input.getOrNull(position + 1) == '=') {
                        position += 2
                        "!="
                    } else {
                        position++
                        throw IllegalArgumentException("Unexpected character: $currentChar")
                    }
                }


                '{' -> {
                    position++
                    "{"
                }

                '}' -> {
                    position++
                    "}"
                }

                '|' -> {
                    position++
                    "|"
                }

                '\'' -> {
                    position++
                    "\'"
                }

                '[' -> {
                    position++
                    "["
                }

                ']' -> {
                    position++
                    "]"
                }

                '.' -> {
                    position++
                    "."
                }

                else -> {
                    position++
                    throw IllegalArgumentException("Unexpected character: $currentChar")
                }
            }
            return Token(TokenType.SYMBOL, symbol)
        }

        return Token(TokenType.EOF, "")
    }

    private fun extractStringLiteral(): String {
        val start = position + 1 // Skip the opening quotation mark
        var end = start
        while (end < input.length && input[end] != '"') {
            end++
        }
        if (end >= input.length) {
            throw IllegalArgumentException("Unterminated string literal")
        }
        val stringLiteral = input.substring(start, end)
        position = end + 1 // Move past the closing quotation mark
        return stringLiteral
    }

    private fun extractNumber(): String {
        val start = position
        while (position < input.length && input[position].isDigit()) {
            position++
        }
        return input.substring(start, position)
    }

    private fun extractIdentifier(): String {
        val start = position
        while (position < input.length && (input[position].isLetterOrDigit() || input[position] == '_')) {
            position++
        }
        return input.substring(start, position)
    }
}
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val sourceCode = StringBuilder()
    while (true) {
        val line = scanner.nextLine()
        if (line.isEmpty()) break
        sourceCode.appendLine(line)
    }

    val lexer = Lexer(sourceCode.toString())
    val tokens = mutableListOf<Token>()
    var token = lexer.getNextToken()
    while (token.type != TokenType.EOF) {
        tokens.add(token)
        token = lexer.getNextToken()
    }

    val parser = Parser(tokens)
    val program = parser.parse()

    val interpreter = Interpreter(program)
    interpreter.interpret()
}

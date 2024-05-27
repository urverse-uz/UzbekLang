import interpreter.Interpreter
import lexer.Lexer
import lexer.Token
import lexer.TokenType
import parser.Parser
import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: java -jar UzbekLang.jar <filename.uzlang>")
        return
    }

    var fileName = args[0]

    if (fileName.endsWith(".txt")) {
        fileName = fileName.removeSuffix(".txt")
    }

    val uzlangFile = File("$fileName.uzlang")
    val sourceCode = if (uzlangFile.exists()) {
        uzlangFile.readText()
    } else {
        val txtFile = File("$fileName.txt")
        if (txtFile.exists()) {
            txtFile.readText()
        } else {
            println("File not found: $fileName.uzlang")
            return
        }
    }

//    println("Source code: $sourceCode")

    val lexer = Lexer(sourceCode)
    val tokens = mutableListOf<Token>()
    var token = lexer.getNextToken()
    while (token.type != TokenType.EOF) {
        tokens.add(token)
        token = lexer.getNextToken()
    }

    println("Tokens: $tokens") // Debug

    val parser = Parser(tokens)
    val program = parser.parse()

    println("Parsed program: $program") // Debug

    val interpreter = Interpreter(program)
    interpreter.interpret()
}

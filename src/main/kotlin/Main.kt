import db.execute
import ql.QLExpressionParser
import java.io.FileNotFoundException

fun main() {
    println("> ")
    var line = readln()
    while (line.isNotBlank() ) {
        try {
            execute(QLExpressionParser(line).parseTerminate())
        } catch (ex: FileNotFoundException) {
            println("File not found.")
        }
        print("> ")
        line = readln()
    }
}


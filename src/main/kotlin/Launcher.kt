import domain.api.LocalExecutor
import domain.ql.ExpressionParser
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

fun main() = runBlocking {
    val executor = LocalExecutor()

    while (true) {
        print("> ")
        val query = readln()
        if (query.lowercase() == "exit") exitProcess(0)
        val result = executor.execute(ExpressionParser(query).parseTerminate(), )
        println(result)
    }
}


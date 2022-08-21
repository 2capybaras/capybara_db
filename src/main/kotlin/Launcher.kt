import core.engine.LocalExecutor
import core.query.QueryParserFactory
import feature.Feature
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

fun main() = runBlocking {
    val executor = LocalExecutor()
    val features: List<Feature> = emptyList()
    val parserFactory = QueryParserFactory(features.map { it.queryCommand })
    val executeCommandMapper = features.associate { it.queryCommand.statement to it.executeCommand }

    while (true) {
        print("> ")
        val query = readln()
        if (query.lowercase() == "exit") exitProcess(0)
        val parser = parserFactory.build(query)
        parser.parse()
        val command = executeCommandMapper[parser.rootStatement] ?: throw IllegalStateException("${parser.rootStatement}")
        val result = executor.execute(command, parser.arguments)
        println(result)
    }
}
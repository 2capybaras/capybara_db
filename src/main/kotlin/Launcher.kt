import core.engine.LocalExecutor
import core.query.QueryParserFactory
import feature.Exit
import feature.Feature
import feature.Scan

fun main() {
    val executor = LocalExecutor()
    val features: List<Feature> = listOf(Scan, Exit)
    val parserFactory = QueryParserFactory(features.map { it.queryCommand })
    val executeCommandMapper = features.associate { it.queryCommand.statement to it.executeCommand }

    while (true) {
        print("> ")
        val query = readln()
        val parser = parserFactory.build(query)
        parser.parse()
        val command = executeCommandMapper[parser.rootStatement] ?: throw IllegalStateException("${parser.rootStatement}")
        val result = executor.execute(command, parser.arguments)
        println(result)
    }
}
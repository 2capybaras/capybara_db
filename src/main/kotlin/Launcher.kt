import core.engine.LocalExecutor
import core.query.QueryParserFactory
import feature.exit.Exit
import feature.Feature
import feature.scan.Scan

fun main() {
    val executor = LocalExecutor()
    val features: List<Feature> = listOf(Scan, Exit)
    val parserFactory = QueryParserFactory(features.map { it.queryCommand })
    val executeCommandMapper = features.associate { it.queryCommand.statement to it.executeCommand }

    while (true) {
        print("> ")
        val query = readln()
        val parser = parserFactory.build(query)
        val parseResult = parser.parse()
        val command = executeCommandMapper[parseResult.rootStatement] ?: throw IllegalStateException("${parseResult.rootStatement}")
        val result = executor.execute(command, parseResult.arguments)
        println(result)
    }
}
package core.query

class QueryParserFactory(commands: List<QueryCommand>) {
    private val statementToToken = commands.associate { it.statement to it.token }

    fun build(query: String) = QueryParser(query, statementToToken)
}
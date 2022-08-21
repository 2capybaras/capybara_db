package core.query

data class QueryParseResult(var rootStatement: String? = null, var arguments: List<Any?> = emptyList())

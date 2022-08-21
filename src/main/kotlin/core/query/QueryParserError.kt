package core.query

import java.text.ParseException

sealed class QueryParserError(message: String, offset: Int): ParseException(message, offset)

object EmptyQueryError: QueryParserError("Query is empty", 0)
object UnsupportedRootTokenError: QueryParserError("Unsupported query command", 0)
class NoTokenError(pos: Int): QueryParserError("No token", pos)